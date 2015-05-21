package org.appfuse.service.hps.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableDouble;
import org.appfuse.dao.hps.HpsElectricChaobiaoDao;
import org.appfuse.dao.hps.HpsElectricChargeDao;
import org.appfuse.dao.hps.HpsHouseDao;
import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsElectricChargeManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.ElectricChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.PaginationQueryParam;
import com.my.hps.webapp.controller.vo.HpsElectricUserTongjiRowView;
import com.my.hps.webapp.model.ElectricChargeState;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.HpsElectricPaymentDate;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.util.SecurityUtil;

@Service
public class HpsElectricChargeManagerImpl extends GenericManagerImpl<HpsElectricChargeRecord, Long>
		implements HpsElectricChargeManager {
	
	private HpsElectricChargeDao chargeDao;
	private HpsHouseDao houseDao;
	private HpsElectricChaobiaoDao chaobiaoDao;
	
	@Override
	@Transactional(readOnly = true)
	public PaginationResult<ElectricChargeState> getChargeStates(
			ElectricChargeQueryParam queryParam) {
		PaginationResult<HpsHouse> houseResult = chargeDao.getHouses(queryParam);
		List<HpsHouse> houses = houseResult.getRows();
		List<Long> houseIds = new ArrayList<Long>();
		for (HpsHouse house : houses) {
			houseIds.add(house.getId());
		}
		List<HpsElectricChaobiao> chaobiaos = chargeDao.getChaobiaos(houseIds);
		List<HpsElectricChaobiao> chaobiaoInputed = new ArrayList<HpsElectricChaobiao>();
		// 最后一次的缴费记录的余额
        MutableDouble lastSurplus = new MutableDouble(0d);
        HpsElectricChargeRecord lastChargeRecord = null;
        boolean lastChargeRecordFound = false;
		for (HpsElectricChaobiao chaobiao : chaobiaos) {
			Long electricCount = chaobiao.getElectricCount();
			// 过滤掉没未抄表的抄表记录
			if (electricCount == null || electricCount <= 0) {
				continue;
			}
			if (lastChargeRecordFound == false && chaobiao.getChargeRecord() == null && lastChargeRecord != null) {
                lastSurplus = new MutableDouble(lastChargeRecord.getCurrentSurplus());
                lastChargeRecordFound = true;
            }
			caculateChaobiaoCharge(chaobiao, lastSurplus);
			chaobiaoInputed.add(chaobiao);
			lastChargeRecord = chaobiao.getChargeRecord();
		}
		List<ElectricChargeState> chargeStates = new ArrayList<ElectricChargeState>();
		for (HpsHouse house : houses) {
			ElectricChargeState chargeState = new ElectricChargeState(house);
			chargeState.setChaobiaoRecords(chaobiaoInputed);
			// 获取缴费状态集合的时候，不计算滞纳金
			chargeState.caculate(false);
			chargeStates.add(chargeState);
		}
		PaginationResult<ElectricChargeState> result = new PaginationResult<ElectricChargeState>();
		result.setRows(chargeStates);
		result.setTotal(houseResult.getTotal());
		return result;
	}

	@Autowired
	public void setChargeDao(HpsElectricChargeDao chargeDao) {
		this.chargeDao = chargeDao;
		this.dao = chargeDao;
	}

	@Autowired
	public void setHouseDao(HpsHouseDao houseDao) {
		this.houseDao = houseDao;
	}

	@Override
	@Transactional(readOnly = true)
	public ElectricChargeState getChargeState(Long houseId, boolean zhinajinOn) {
		HpsHouse house = houseDao.getHouse(houseId);
		List<Long> houseIds = new ArrayList<Long>(1);
		houseIds.add(houseId);
		List<HpsElectricChaobiao> chaobiaos = chargeDao.getChaobiaos(houseIds);
		List<HpsElectricChaobiao> chaobiaoInputed = new ArrayList<HpsElectricChaobiao>();
		// 最后一次的缴费记录的余额
		MutableDouble lastSurplus = new MutableDouble(0d);
		HpsElectricChargeRecord lastChargeRecord = null;
		boolean lastChargeRecordFound = false;
		for (HpsElectricChaobiao chaobiao : chaobiaos) {
			Long electricCount = chaobiao.getElectricCount();
			// 过滤掉没未抄表的抄表记录
			if (electricCount == null || electricCount <= 0) {
				continue;
			}
			if (lastChargeRecordFound == false && chaobiao.getChargeRecord() == null && lastChargeRecord != null) {
                lastSurplus = new MutableDouble(lastChargeRecord.getCurrentSurplus());
                lastChargeRecordFound = true;
            }
			caculateChaobiaoCharge(chaobiao, lastSurplus);
			chaobiaoInputed.add(chaobiao);
			lastChargeRecord = chaobiao.getChargeRecord();
		}
		ElectricChargeState chargeState = new ElectricChargeState(house);
		chargeState.setChaobiaoRecords(chaobiaoInputed);
		chargeState.caculate(zhinajinOn);
		return chargeState;
	}

	@Override
	@Transactional
	public HpsElectricChargeRecord charge(Long houseId, Double chargeValue, boolean zhinajinOn) {
		// 缴费之前的状态
		ElectricChargeState stateSaveBefore = getChargeState(houseId, zhinajinOn);
		// 未缴费记录
		List<HpsElectricChaobiao> arrearageChaobiaoRecords = stateSaveBefore.getArrearageChaobiaoRecords();
		// 余额超过部分记录
		List<HpsElectricChaobiao> suplusChaobiaoRecords = stateSaveBefore.getSurplusChaobiaoRecords();
		List<HpsElectricChaobiao> chaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
		chaobiaoRecords.addAll(suplusChaobiaoRecords);
		chaobiaoRecords.addAll(arrearageChaobiaoRecords);
//		if (arrearageChaobiaoRecords.size() == 0) {
//			throw new RuntimeException("该户主[" + stateSaveBefore.getHouse().getOwner().getName()
//					+ "]没有未缴费抄表记录");
//		}
		double surplus = chargeValue + stateSaveBefore.getSurplus();
		BigDecimal bigDecimal = new BigDecimal(surplus);
		surplus = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (surplus < 0) {
			throw new RuntimeException("缴费金额不足，应缴：" + -stateSaveBefore.getSurplus() +
					"元，实缴：" + chargeValue + "元");
		}
		HpsElectricChargeRecord chargeRecord = new HpsElectricChargeRecord();
		chargeRecord.setHouse(stateSaveBefore.getHouse());
		chargeRecord.setBase(stateSaveBefore.getHouse().getLouzuo().getArea().getBase());
		chargeRecord.setHouseOwner(stateSaveBefore.getHouse().getOwner());
		chargeRecord.setCurrentSurplus(surplus);
		HpsElectricChargeRecord previousRecord = stateSaveBefore.getLastElectricChargeRecord();
		if (previousRecord != null) {
			chargeRecord.setPreviousSurplus(previousRecord.getCurrentSurplus());
		} else {
			chargeRecord.setPreviousSurplus(0d);
		}
		chargeRecord.setChargeDate(new Date());
		chargeRecord.setChargeUser(SecurityUtil.getCurrentUser());
		chargeRecord.setActualCharge(chargeValue);
		//chargeRecord.setMustCharge(-stateSaveBefore.getSurplus());
		double mustCharge = 0d;
		chargeRecord.setMustCharge(-stateSaveBefore.getSurplus());
		for (HpsElectricChaobiao chaobiao : chaobiaoRecords) {
			chaobiao.setChargeRecord(chargeRecord);
			// 电费
			mustCharge += chaobiao.getElectricCharge();
			// 卫生费
			Double weisheng = chaobiao.getWeishengCharge();
			if (weisheng != null) {
				mustCharge += chaobiao.getWeishengCharge();
			}
			// 排污费
			Double paiwu = chaobiao.getPaiwuCharge();
			if (paiwu != null) {
				mustCharge += chaobiao.getPaiwuCharge();
			}
			// 照明费
			Double zhaoming = chaobiao.getZhaomingCharge();
			if (zhaoming != null) {
				mustCharge += chaobiao.getZhaomingCharge();
			}
		}
		chargeRecord.setMustCharge(mustCharge);
		chargeRecord.setCancelled(false);
		chargeRecord.setChaobiaoSet(new HashSet<HpsElectricChaobiao>(chaobiaoRecords));
		chargeRecord.setZhinajinOn(zhinajinOn);
		return chargeDao.save(chargeRecord);
	}

	private void caculateChaobiaoCharge(HpsElectricChaobiao chaobiao, MutableDouble lastSurplus) {
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
		// 滞纳金
		Double zhinajin = caculateZhinajin(chaobiao, lastSurplus.doubleValue());
		lastSurplus.setValue(lastSurplus.doubleValue() - electricCharge);
		
		chaobiao.setZhinajin(zhinajin);
		Double paiwufei = unit.getPaiwufei();
		if (paiwufei == null) {
			paiwufei = 0d;
		}
		chaobiao.setPaiwuCharge(paiwufei);
		Double weishengfei = unit.getWeishengfei();
		if (weishengfei == null) {
			weishengfei = 0d;
		}
		chaobiao.setWeishengCharge(weishengfei);
		Double zhaomingfei = unit.getZhaomingfei();
		if (zhaomingfei == null) {
			zhaomingfei = 0d;
		}
		chaobiao.setZhaomingCharge(zhaomingfei);
	}

	
	private Double caculateSingleCharge(HpsElectricChaobiao chaobiao) {
		HpsElectricUnit unit = chaobiao.getUnit();
		Long electricCount = chaobiao.getElectricCount();
		BigDecimal bigDecimal = new BigDecimal(electricCount * unit.getUnit());
		return bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
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
	
	private double caculateZhinajin(HpsElectricChaobiao chaobiao, double previousSurplus) {
		if (chaobiao.getChargeRecord() != null) {
			// 已经缴费，直接返回交费时记录下来的滞纳金
			return chaobiao.getZhinajin();
		} else {
			// 电费缴费日期设定
			HpsElectricPaymentDate paymentDate = chaobiao.getPaymentDate();
			// 缴费结束日期
			Date endDate = paymentDate.getEndDate();
			Date currentDate = new Date(System.currentTimeMillis());
			int days = (int) ((currentDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
			if (days <= 0) { // 没有逾期
				return 0d;
			} else {
				chaobiao.setZhinajinDayCount(days);
				Double scale = chaobiao.getUnit().getZhinaScale();
				// 滞纳金比例
				if (scale != null && scale > 0) {
				    Double zhinajin = 0d;
				    if (previousSurplus <= 0) {
				        // 上次余额没有余额
				        zhinajin = days * scale * chaobiao.getElectricCharge();
				    } else {
				        // 上次余额有余额
				        double currentSurplus = previousSurplus - chaobiao.getElectricCharge();
				        if (currentSurplus < 0) {
				            // 已欠费
				            zhinajin = days * scale * (-currentSurplus);
				        }
				    }
					// 注意此处可能存在误差，需要在调用处处理
					return zhinajin;
				} else {
					return 0d;
				}
			}
		}
	}
	
	@Override
	@Transactional
	public HpsElectricChargeRecord cancelChargeRecord(Long recordId, String cause) {
		HpsElectricChargeRecord record = chargeDao.get(recordId);
		for (HpsElectricChaobiao chaobiao : record.getChaobiaoSet()) {
			chaobiao.setChargeRecord(null);
			chaobiao.setZhinajin(0d);
		}
		try {
			while (cause.getBytes("UTF-8").length > 500) {
				cause = cause.substring(0, cause.length() - 1);
			}
		} catch (Exception e) {}
		record.setCancelledCause(cause);
		record.setCancelled(true);
		record.getChaobiaoSet().clear();
		save(record);
		return record;
	}

	@Override
	@Transactional(readOnly = true)
	public HpsElectricChargeRecord get(Long id) {
		HpsElectricChargeRecord record = super.get(id);
		// lazy init
		record.getChaobiaoSet().size();
		return record;
	}
	
	@Override
	@Transactional(readOnly = true)
	public PaginationResult<HpsElectricChargeRecord> getChargeRecords(Long houseId, PaginationQueryParam param) {
		return chargeDao.getChargeRecords(houseId, param);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PaginationResult<HpsElectricChargeRecord> getChargeRecords(ElectricTongjiQueryParam param) {
		PaginationResult<HpsElectricChargeRecord> result = chargeDao.getChargeRecords(param);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<HpsElectricUserTongjiRowView> getUserTongjiRowList(
			ElectricTongjiQueryParam param) {
		param.setPage(1);
		param.setRows(Integer.MAX_VALUE);
		List<HpsElectricChargeRecord> records = chargeDao.getChargeRecords(param).getRows();
		List<HpsElectricUserTongjiRowView> result = new ArrayList<HpsElectricUserTongjiRowView>();
		Map<Long, HpsElectricUserTongjiRowView> viewMap = new HashMap<Long, HpsElectricUserTongjiRowView>();
		for (HpsElectricChargeRecord record : records) {
			HpsUser operUser = record.getChargeUser();
			Long operUserId = operUser.getId();
			HpsElectricUserTongjiRowView userView = viewMap.get(operUserId);
			if (userView == null) {
				userView = new HpsElectricUserTongjiRowView();
				userView.setOperUser(operUser);
				userView.setOperName(operUser.getUserName());
				viewMap.put(operUserId, userView);
			}
			Long juminMonthCount = userView.getJuminMonthCount();
			if (juminMonthCount == null) {
				juminMonthCount = 0l;
			}
			//double electricCharge = userView.getElectricCharge();
			double zhinajin = userView.getZhinajin();
			double weishengfei = userView.getWeishengfei();
			double paiwufei = userView.getPaiwufei();
			double zhaomingfei = userView.getZhaomingfei();
			long electricCount = userView.getElectricCount();
	        int receiptCnt = userView.getReceiptCnt();
			for (HpsElectricChaobiao chaobiao : record.getChaobiaoSet()) {
				if (!chaobiao.getHouse().getYongfangXingzhi().getCode().equals(HpsDictManager.YONGFANG_XINGZHI_SHANGYONG)) {
					juminMonthCount++;
				}
				//Double rowElectricCharge = chaobiao.getElectricCharge();
				Double rowZhinajin = chaobiao.getZhinajin();
				Double rowWeishengfei = chaobiao.getWeishengCharge();
				Double rowPaiwufei = chaobiao.getPaiwuCharge();
				Double rowZhaomingfei = chaobiao.getZhaomingCharge();
				long rowElectricCount = chaobiao.getElectricCount();
//				if (rowElectricCharge != null) {
//					electricCharge += rowElectricCharge;
//				}
				if (record.isZhinajinOn() && rowZhinajin != null) {
					zhinajin += rowZhinajin;
				}
				if (rowWeishengfei != null) {
					weishengfei += rowWeishengfei;
				}
				if (rowPaiwufei != null) {
					paiwufei += rowPaiwufei;
				}
				if (rowZhaomingfei != null) {
					zhaomingfei += rowZhaomingfei;
				}
				electricCount += rowElectricCount;
			}
			int chaobiaoSize = record.getChaobiaoSet().size();
			if (chaobiaoSize <= 3) {
				receiptCnt++;
			} else if (chaobiaoSize <= 11) {
				receiptCnt += 2;
			} else {
				receiptCnt += 3;
			}
			userView.setReceiptCnt(receiptCnt);
			userView.setPaiwufei(paiwufei);
			userView.setWeishengfei(weishengfei);
			userView.setZhaomingfei(zhaomingfei);
			userView.setZhinajin(zhinajin);
			userView.setElectricCount(electricCount);
			double actualCharge = userView.getActualCharge();
			actualCharge += record.getActualCharge();
			userView.setActualCharge(actualCharge);
			userView.setJuminMonthCount(juminMonthCount);
			userView.setElectricCharge(actualCharge - paiwufei - weishengfei - zhaomingfei - zhinajin);
		}
		result.addAll(viewMap.values());
		return result;
	}
	
	@Autowired
	public void setElectricDao(HpsElectricChaobiaoDao electricDao) {
		this.chaobiaoDao = electricDao;
	}

	@Override
	@Transactional
	public HpsElectricChargeRecord initHouseSurplus(Long houseId,
			Double initSurplus, Integer initElectricReadout) {
		HpsHouse house = houseDao.get(houseId);
		List<HpsElectricUnit> units = chaobiaoDao.getElectricUnits(house
				.getLouzuo().getArea().getBase().getCode());
		HpsElectricUnit unit = HpsElectricChaobiaoManagerImpl.getHouseElectricUnit(house, units);
		HpsElectricChaobiao initChaobiao = new HpsElectricChaobiao();
		initChaobiao.setHouse(house);
		initChaobiao.setHouseOwner(house.getOwner());
		initChaobiao.setUnit(unit);
		String initDateStr = "2014-01-01";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date initDate = null;
		try {
			initDate = format.parse(initDateStr);
		} catch (ParseException e) {
			
		}
		HpsElectricPaymentDate paymentDate = chaobiaoDao.getPaymentDate(house.getLouzuo().getArea()
				.getBase().getId(), "2014-01");
		initChaobiao.setReadMeterDate(initDate);
		initChaobiao.setProvReadoutsElectric(0l);
		initChaobiao.setReadoutsElectric(0l);
		initChaobiao.setNewReadoutsElectric(initElectricReadout.longValue());
		initChaobiao.setPaymentDate(paymentDate);
		initChaobiao.setElectricCount(1l);
		initChaobiao.setElectricCharge(0d);
		initChaobiao.setPaiwuCharge(0d);
		initChaobiao.setZhaomingCharge(0d);
		initChaobiao.setZhinajin(0d);
		initChaobiao.setZhinajinDayCount(0);
		initChaobiao.setWeishengCharge(0d);
		
		HpsElectricChargeRecord chargeRecord = new HpsElectricChargeRecord();
		chargeRecord.setActualCharge(0d);
		chargeRecord.setChargeDate(initDate);
		chargeRecord.setChargeUser(SecurityUtil.getCurrentUser());
		chargeRecord.setCurrentSurplus(initSurplus);
		chargeRecord.setCancelled(false);
		chargeRecord.setZhinajinOn(false);
		chargeRecord.setPreviousSurplus(0d);
		chargeRecord.setHouse(house);
		chargeRecord.setMustCharge(0d);
		chargeRecord.setHouseOwner(house.getOwner());
		Set<HpsElectricChaobiao> chaobiaos = new HashSet<HpsElectricChaobiao>();
		chaobiaos.add(initChaobiao);
		chargeRecord.setChaobiaoSet(chaobiaos);
		initChaobiao.setChargeRecord(chargeRecord);
		return save(chargeRecord);
	}


}
