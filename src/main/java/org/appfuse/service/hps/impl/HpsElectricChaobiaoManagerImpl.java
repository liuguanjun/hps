package org.appfuse.service.hps.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hps.HpsBaseDao;
import org.appfuse.dao.hps.HpsElectricChaobiaoDao;
import org.appfuse.dao.hps.HpsElectricPaymentDateDao;
import org.appfuse.dao.hps.HpsHouseDao;
import org.appfuse.service.hps.HpsElectricChaobiaoManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.ElectricChaobiaoQueryParam;
import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoPaginationResult;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricPaymentDate;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsLouzuo;

@Service
public class HpsElectricChaobiaoManagerImpl extends GenericManagerImpl<HpsElectricChaobiao, Long> implements HpsElectricChaobiaoManager {

	private HpsElectricChaobiaoDao chaobiaoDao;
	private HpsBaseDao baseDao;
	private HpsHouseDao houseDao;
	private HpsElectricPaymentDateDao paymentDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<HpsElectricChaobiao> getChaobiaos(Long paymentDateId) {
		List<HpsElectricChaobiao> chaobiaos = chaobiaoDao.getChaobiaos(paymentDateId);
		return chaobiaos;
	}
	
	@Override
	@Transactional
	public List<HpsElectricChaobiao> updateElectricChaobiao(
			List<HpsElectricChaobiao> electricityChaobiaoList) {
		Date currentDate = new Date();
		// 此处没有设置电费的值，在缴费时候会计算电费，并保存到相应的抄表记录中
		for (HpsElectricChaobiao chaobiao : electricityChaobiaoList) {
			Long currentEleReadout = chaobiao.getReadoutsElectric();
			Long previousEleReadout = chaobiao.getProvReadoutsElectric();
			// 用电量
			Long eleCount = currentEleReadout - previousEleReadout;
			if (eleCount <= 0) {
				chaobiao.setWeishengCharge(0d);
				chaobiao.setZhaomingCharge(0d);
				chaobiao.setPaiwuCharge(0d);
			}
			chaobiao.setElectricCount(eleCount);
			// 抄表日期
			chaobiao.setReadMeterDate(currentDate);
			// 计算电价
			caculateChaobiaoCharge(chaobiao);
		}
		@SuppressWarnings("unchecked")
		List<HpsElectricChaobiao> hpsElectricityList = chaobiaoDao
				.updateElectricChaobiao(electricityChaobiaoList);
		return hpsElectricityList;
	}
	
	@Autowired
	public void setElectricDao(HpsElectricChaobiaoDao electricDao) {
		this.dao = electricDao;
		this.chaobiaoDao = electricDao;
	}
	
	@Autowired
	public void setHouseDao(HpsHouseDao houseDao) {
		this.houseDao = houseDao;
	}

	@Autowired
	public void setBaseDao(HpsBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	@Transactional(readOnly = true)
	public HpsElectricPaymentDate getElectricPaymentDate(Long paymentDateId) {
		return chaobiaoDao.getElectricPaymentDate(paymentDateId);
	}

	@Override
	@Transactional(readOnly = true)
	public ElectricChaobiaoPaginationResult getChaobiaos(
			ElectricChaobiaoQueryParam param) {
		return chaobiaoDao.getChaobiaos(param);
	}

	@Override
	@Transactional
	public void initializeChaobiaoRecords() {
		List<HpsBase> allBases = baseDao.getAll();
		for (HpsBase base : allBases) {
			// 此处返回的缴费日期List是按照收取月份升序排列的
			List<HpsElectricPaymentDate> allPaymentDates = chaobiaoDao.getPaymentDates(base.getCode());
			HpsElectricPaymentDate previousInitPaymentDate = null;
			for (HpsElectricPaymentDate paymentDate : allPaymentDates) {
				Calendar nowCalendar = new GregorianCalendar();
				Calendar paymentDateCalendar = new GregorianCalendar();
				paymentDateCalendar.setTime(paymentDate.getMonth());
				//int nowMonth = nowCalendar.get(Calendar.MONTH);
				//int paymentDateMonth = paymentDateCalendar.get(Calendar.MONTH);
				int dayOfMonth = nowCalendar.get(Calendar.DAY_OF_MONTH);
				nowCalendar.add(Calendar.MONTH, -1);
				
				if (paymentDateCalendar.get(Calendar.MONTH) == nowCalendar.get(Calendar.MONTH) &&
				        paymentDateCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) && dayOfMonth > 3) {
					// 只初始化当前月份之前的抄表记录
					initializeChaobiaoRecords(paymentDate, previousInitPaymentDate);
					// 只初始化一个月份
					break;
				}
				if (paymentDate.isChaobiaosInitialized()) {
					// 已经初始化过的月份，不必要再次初始化
					previousInitPaymentDate = paymentDate;
				}
			}
		}
	}
	
	private void initializeChaobiaoRecords(HpsElectricPaymentDate initPaymentDate, HpsElectricPaymentDate previousPaymentDate) {
		// 获取所有的房屋信息
		HpsBase base = initPaymentDate.getBase();
		HouseQueryParam houseQueryParam = new HouseQueryParam();
		houseQueryParam.setBaseCode(base.getCode());
		houseQueryParam.setRows(Integer.MAX_VALUE);
		List<HpsHouse> houses = houseDao.getHouses(houseQueryParam).getRows();
		Map<Long, HpsHouse> houseMap = new HashMap<Long, HpsHouse>();
		for (HpsHouse house : houses) {
			houseMap.put(house.getId(), house);
		}
		// 获取所有的电费单价
		List<HpsElectricUnit> units = chaobiaoDao.getElectricUnits(base.getCode());
		boolean chaobiaoInitialized = false;
		if (!initPaymentDate.isChaobiaosInitialized()) {
			// 获取上一个月的抄表信息
			Map<Long, HpsElectricChaobiao> housePreviousChaobiaoMap = new HashMap<Long, HpsElectricChaobiao>();
			List<HpsElectricChaobiao> previousChaobiaos = new ArrayList<HpsElectricChaobiao>();
			if (previousPaymentDate != null) {
				previousChaobiaos = getChaobiaos(previousPaymentDate.getId());
				for (HpsElectricChaobiao previousChaobiao : previousChaobiaos) {
					housePreviousChaobiaoMap.put(previousChaobiao.getHouse().getId(), previousChaobiao);
				}
			}
			// 根据上个月的抄表记录生成当月的抄表记录
			for (HpsElectricChaobiao previousChaobiao : previousChaobiaos) {
				HpsHouse house = houseMap.get(previousChaobiao.getHouse().getId());
				if (house == null) {
					// 房屋已经不存在，房屋已经被删除
					continue;
				}
				HpsElectricUnit unit = getHouseElectricUnit(house, units);
				if (unit == null) {
					continue;
				}
				// 上次抄表日期
				Long readoutsElectric = previousChaobiao.getReadoutsElectric();
				Long newReadoutElectric = previousChaobiao.getNewReadoutsElectric();
				Long provReadoutsElectric = previousChaobiao.getProvReadoutsElectric();
				HpsElectricChaobiao currentChaobiao = new HpsElectricChaobiao();
				if (newReadoutElectric != null) {
					currentChaobiao.setProvReadoutsElectric(newReadoutElectric);
				} else {
					if (readoutsElectric != null) {
						currentChaobiao.setProvReadoutsElectric(readoutsElectric);
					} else {
						// 当上期抄表记录的表变更抄值和本期表值都为空时，使用上期抄表的表初值作为本月的初值
						// 此处时针对上月没有电量的住户进行的特殊处理
						currentChaobiao.setProvReadoutsElectric(provReadoutsElectric);
					}
				}
				currentChaobiao.setUnit(unit);
				currentChaobiao.setHouse(house);
				currentChaobiao.setHouseOwner(house.getOwner());
				currentChaobiao.setPaiwuCharge(unit.getPaiwufei());
				currentChaobiao.setWeishengCharge(unit.getWeishengfei());
				currentChaobiao.setZhaomingCharge(unit.getZhaomingfei());
				currentChaobiao.setPaymentDate(initPaymentDate);
				save(currentChaobiao);
				if (!chaobiaoInitialized) {
					chaobiaoInitialized = true;
				}
			}
			// 根据房屋生成抄表记录(每个月都有可能由新增的房屋，在上个月的抄表记录中，并没有体现)
			for (HpsHouse house : houses) {
				HpsElectricChaobiao previousChaobiao = housePreviousChaobiaoMap.get(house.getId());
				if (previousChaobiao != null) {
					// 上个月中已经存在该房屋的抄表记录，已经在上面进行了处理，此处跳过
					continue;
				}
				HpsElectricUnit unit = getHouseElectricUnit(house, units);
				if (unit == null) {
					continue;
				}
				HpsElectricChaobiao currentChaobiao = new HpsElectricChaobiao();
				currentChaobiao.setUnit(unit);
				currentChaobiao.setHouse(house);
				currentChaobiao.setHouseOwner(house.getOwner());
				currentChaobiao.setPaiwuCharge(unit.getPaiwufei());
				currentChaobiao.setWeishengCharge(unit.getWeishengfei());
				currentChaobiao.setZhaomingCharge(unit.getZhaomingfei());
				currentChaobiao.setPaymentDate(initPaymentDate);
				save(currentChaobiao);
				if (!chaobiaoInitialized) {
					chaobiaoInitialized = true;
				}
			}
			if (chaobiaoInitialized) {
				initPaymentDate.setChaobiaosInitialized(true);
				paymentDao.save(initPaymentDate);
			}
		} else {
			Map<Long, HpsElectricChaobiao> houseCurrentChaobiaoMap = new HashMap<Long, HpsElectricChaobiao>();
			List<HpsElectricChaobiao> currentChaobiaos = getChaobiaos(initPaymentDate.getId());
			List<Long> provReadoutsElectricEmptyHouseIds = new ArrayList<Long>();
			for (HpsElectricChaobiao chaobiao : currentChaobiaos) {
				houseCurrentChaobiaoMap.put(chaobiao.getHouse().getId(), chaobiao);
				Long electricCount = chaobiao.getElectricCount();
				Double electricCharge = chaobiao.getElectricCharge();
				if (electricCount != null && electricCount > 0 && electricCharge == null) {
					caculateChaobiaoCharge(chaobiao);
				}
				if (chaobiao.getProvReadoutsElectric() == null) {
				    provReadoutsElectricEmptyHouseIds.add(chaobiao.getHouse().getId());
				}
			}
			for (HpsHouse house : houses) {
				HpsElectricChaobiao chaobiao = houseCurrentChaobiaoMap
						.get(house.getId());
				if (chaobiao != null) {
					// 已经存在该房屋的抄表记录，跳过
					continue;
				}
				HpsElectricUnit unit = getHouseElectricUnit(house, units);
				if (unit == null) {
					continue;
				}
				HpsElectricChaobiao currentChaobiao = new HpsElectricChaobiao();
				currentChaobiao.setUnit(unit);
				currentChaobiao.setHouse(house);
				currentChaobiao.setHouseOwner(house.getOwner());
				currentChaobiao.setPaiwuCharge(unit.getPaiwufei());
				currentChaobiao.setWeishengCharge(unit.getWeishengfei());
				currentChaobiao.setZhaomingCharge(unit.getZhaomingfei());
				currentChaobiao.setPaymentDate(initPaymentDate);
				save(currentChaobiao);
			}
			// 补足上月没有用电量的住户当月的抄表初值
			// 获取上一次有抄表的抄表记录
			List<HpsElectricChaobiao> previousInputedChaobiaos = chaobiaoDao.getPreviousInputedChaobiaos(initPaymentDate.getId(), 
			        provReadoutsElectricEmptyHouseIds);
			for (HpsElectricChaobiao previousChaobiao : previousInputedChaobiaos) {
				HpsElectricChaobiao chaobiao = houseCurrentChaobiaoMap
						.get(previousChaobiao.getHouse().getId());
				if (chaobiao == null) {
					// 以防万一，正常来说，上月有抄表，在本月也有
					continue;
				}
				Long readoutsElectric = previousChaobiao.getReadoutsElectric();
				Long newReadoutElectric = previousChaobiao.getNewReadoutsElectric();
				Long provReadoutsElectric = previousChaobiao.getProvReadoutsElectric();
				if (readoutsElectric == null && newReadoutElectric == null && provReadoutsElectric == null) {
					continue;
				}
				if (chaobiao.getProvReadoutsElectric() == null) {
					if (newReadoutElectric != null) {
						chaobiao.setProvReadoutsElectric(newReadoutElectric);
					} else {
						if (readoutsElectric != null) {
							chaobiao.setProvReadoutsElectric(readoutsElectric);
						} else {
							// 当上期抄表记录的表变更抄值和本期表值都为空时，使用上期抄表的表初值作为本月的初值
							// 此处时针对上月没有电量的住户进行的特殊处理
							chaobiao.setProvReadoutsElectric(provReadoutsElectric);
						}
					}
					save(chaobiao);
				}
			}
		}
		
	}

	public static HpsElectricUnit getHouseElectricUnit(HpsHouse house, List<HpsElectricUnit> units) {
		StringBuilder houseKeyBuilder = new StringBuilder();
		houseKeyBuilder.append(house.getLouzuo().getArea().getBase().getId()).append("-")
			.append(house.getYongfangXingzhi().getId()).append("-")
			.append(house.getLouzuo().getArea().getId()).append("-")
			.append(house.getLouzuo().getId()).append("-")
			.append(house.getDanyuan()).append("-")
			.append(house.getCeng()).append("-")
			.append(house.getDoorNo());
		StringBuilder unitKeyBuilder = new StringBuilder();
		while (true) {
			for (HpsElectricUnit unit : units) {
				unitKeyBuilder.append(unit.getBase().getId()).append("-");
				HpsDictItem yongfangXingzhi = unit.getYongfangXingzhi();
				if (yongfangXingzhi != null) {
					unitKeyBuilder.append(yongfangXingzhi.getId()).append("-");
				}
				HpsArea area = unit.getArea();
				if (area != null) {
					unitKeyBuilder.append(area.getId()).append("-");
				}
				HpsLouzuo louzuo = unit.getLouzuo();
				if (louzuo != null) {
					unitKeyBuilder.append(louzuo.getId()).append("-");
					String danyuan = unit.getDanyuan();
					if (StringUtils.isNotEmpty(danyuan)) {
						unitKeyBuilder.append(danyuan).append("-");
					}
					String ceng = unit.getCeng();
					if (StringUtils.isNotEmpty(ceng)) {
						unitKeyBuilder.append(ceng).append("-");
					}
					String doorNo = unit.getDoorNo();
					if (StringUtils.isNotEmpty(doorNo)) {
						unitKeyBuilder.append(doorNo).append("-");
					}
				}
				int lastIndex = unitKeyBuilder.length() - 1;
				unitKeyBuilder.deleteCharAt(lastIndex);
				if (unitKeyBuilder.toString().equals(houseKeyBuilder.toString())) {
					return unit;
				}
				unitKeyBuilder.delete(0, unitKeyBuilder.length());
			}
			int lastIndexOfSuffix = houseKeyBuilder.lastIndexOf("-");
			if (lastIndexOfSuffix >= 0) {
				houseKeyBuilder.delete(lastIndexOfSuffix, houseKeyBuilder.length());
			} else {
				break;
			}
		}
		return null;
	}


	@Autowired
	public void setPaymentDao(HpsElectricPaymentDateDao paymentDao) {
		this.paymentDao = paymentDao;
	}
	
	// 以下内容从HpsElectricChargeManagerImpl中拷贝过来，有时间需要重构成一份代码
	private void caculateChaobiaoCharge(HpsElectricChaobiao chaobiao) {
		if (chaobiao.getChargeRecord() != null) { // 已缴费的不计算
			return;
		}
		Double electricCharge = null;
		//HpsElectricUnit unit = chaobiao.getUnit();
		// 重新获取最新单价
		// 获取所有的电费单价
		List<HpsElectricUnit> units = chaobiaoDao.getElectricUnits(chaobiao.getHouse()
				.getLouzuo().getArea().getBase().getCode());
		HpsElectricUnit unit = HpsElectricChaobiaoManagerImpl.getHouseElectricUnit(chaobiao.getHouse(), units);
		chaobiao.setUnit(unit);
		if (unit.isLevel()) { // 阶梯电价
			electricCharge = caculateLevelCharge(chaobiao);
		} else {
			electricCharge = caculateSingleCharge(chaobiao);
		}
		// 电费
		BigDecimal bigDecimal = new BigDecimal(electricCharge.toString());
		chaobiao.setElectricCharge(bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	/**
	 * 计算阶梯电价
	 * 
	 * @param levelUnit
	 */
	private Double caculateLevelCharge(HpsElectricChaobiao chaobiao) {
		HpsElectricUnit levelUnit = chaobiao.getUnit();
		Long electricCount = chaobiao.getElectricCount();
		Integer levelCount_1 = getLevel1Count(levelUnit, electricCount);
		Integer levelCount_2 = getLevel2Count(levelUnit, electricCount);
		Integer levelCount_3 = getLevel3Count(levelUnit, electricCount);
		Double electricCharge = 0d;
		if (levelCount_1 != 0) {
			electricCharge += levelCount_1 * levelUnit.getUnit1();
		}
		if (levelCount_2 != 0) {
			electricCharge += levelCount_2 * levelUnit.getUnit2();
		}
		if (levelCount_3 != 0) {
			electricCharge += levelCount_3 * levelUnit.getUnit3();
		}
		BigDecimal bigDecimal = new BigDecimal(electricCharge);
		electricCharge = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return electricCharge;
	}
	
	private Integer getLevel1Count(HpsElectricUnit levelUnit, Long electricCount) {
		Integer levelEnd_1 = levelUnit.getEnd1();
		if (electricCount <= levelEnd_1) {
			// 用电量还没超过阶梯1的结束
			return electricCount.intValue();
		} else {
			// 超过阶梯1的结束
			return levelEnd_1;
		}
	}
	
	private Integer getLevel2Count(HpsElectricUnit levelUnit, Long electricCount) {
		Integer levelStart_2 = levelUnit.getStart2();
		Integer levelEnd_2 = levelUnit.getEnd2();
		if (levelEnd_2 == null) {
			levelEnd_2 = Integer.MAX_VALUE;
		}
		if (electricCount < levelStart_2) {
			// 没到阶梯2的开始
			return 0;
		} else {
			if (electricCount <= levelEnd_2) {
				// 用电量还没超过阶梯2的结束
				return electricCount.intValue() - levelStart_2 + 1;
			} else {
				// 超过阶梯2的开始
				return levelEnd_2 - levelStart_2 + 1;
			}
		}
	}

	private Integer getLevel3Count(HpsElectricUnit levelUnit, Long electricCount) {
		Integer levelStart_3 = levelUnit.getStart3();
		Integer levelEnd_3 = levelUnit.getEnd3();
		if (levelEnd_3 == null) {
			levelEnd_3 = Integer.MAX_VALUE;
		}
		if (electricCount < levelStart_3) {
			// 没到阶梯3的开始
			return 0;
		} else {
			if (electricCount <= levelEnd_3) {
				// 用电量还没超过阶梯3的结束
				return electricCount.intValue() - levelStart_3 + 1;
			} else {
				// 超过阶梯3的开始
				return levelEnd_3 - levelStart_3 + 1;
			}
		}
	}
	
	private Double caculateSingleCharge(HpsElectricChaobiao chaobiao) {
		HpsElectricUnit unit = chaobiao.getUnit();
		Long electricCount = chaobiao.getElectricCount();
		BigDecimal bigDecimal = new BigDecimal(electricCount * unit.getUnit());
		return bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	
}
