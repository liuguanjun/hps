package org.appfuse.service.hps.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.appfuse.dao.hps.HpsHeatingChargeDao;
import org.appfuse.service.hps.HpsHeatingChargeManager;
import org.appfuse.service.hps.HpsHouseManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.model.HeatingChargeRecordPaginationResult;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;
import com.my.hps.webapp.util.SecurityUtil;

@Service
public class HpsHeatingChargeManagerImpl extends GenericManagerImpl<HpsHeatingChargeRecord, Long>
		implements HpsHeatingChargeManager {
	
	private HpsHeatingChargeDao chargeDao;
	private HpsHouseManager houseManager;

	@Override
	@Transactional(readOnly = true)
	public List<HpsHeatingPaymentDate> getPaymentDates(String baseCode) {
		List<HpsHeatingPaymentDate> paymentDates = chargeDao.getPaymentDates(baseCode);
		return paymentDates;
	}
	
	@Override
	@Transactional(readOnly = true)
	public HeatingChargeRecordPaginationResult getChargeRecords(HeatingChargeQueryParam queryParam) {
	    HeatingChargeRecordPaginationResult result = chargeDao.getChargeRecords(queryParam);
		List<HpsHeatingChargeRecord> records = result.getRows();
		setRecordProperties(records);
		return result;
	}

	@Autowired
	public void setChargeDao(HpsHeatingChargeDao chargeDao) {
		this.chargeDao = chargeDao;
	}

	@Override
	@Transactional
	public void initializeChargeRecords() {
		List<HpsHeatingPaymentDate> paymentDates = chargeDao.getPaymentDates();
		for (HpsHeatingPaymentDate paymentDate : paymentDates) {
			if (paymentDate.isChargeRecordsInitialized()) {
				// 已经初始过了
				continue;
			}
			Long startDateTimeMillis = paymentDate.getPayStartDate().getTime();
			Long currentTimeMillis = System.currentTimeMillis();
			if (startDateTimeMillis - currentTimeMillis <= 3000 * 60 * 60 * 24) {
				// 缴费日期的前三天，对数据进行初始化
				initializeChargeRecords(paymentDate);
				paymentDate.setChargeRecordsInitialized(true);
			}
		}
	}
	
	@Override
	@Transactional
	public void initializeChargeRecordCurrentYear(HpsHouse house) {
		//Calendar currentCalendar = new GregorianCalendar();
		//int currentYear = currentCalendar.get(Calendar.YEAR);
		List<HpsHeatingPaymentDate> paymentDates = getPaymentDates(house.getLouzuo()
				.getArea().getBase().getCode());
		//HpsHeatingPaymentDate currentPaymentDate = null;
		for (HpsHeatingPaymentDate paymentDate : paymentDates) {
//			Calendar startCalendar = new GregorianCalendar();
//			startCalendar.setTime(paymentDate.getPayStartDate());
//			if (startCalendar.get(Calendar.YEAR) == currentYear) {
//				currentPaymentDate = paymentDate;
//				break;
//			}
	        if (!paymentDate.isChargeRecordsInitialized()) {
	            // 当前缴费日期相关的缴费记录还没有初始化，不单独针对此房屋进行初始化
	            // 直接返回，等待缴费日期初始化时统一进行缴费记录的初始化
	            return;
	        }
	        List<HpsHeatingChargeRecord> chargeRecords = chargeDao.getChargeRecords(
	                paymentDate.getId(), house.getId());
	        if (chargeRecords.size() == 0) { 
	            // 缴费日期已经初始化，但是仍然没有关于此房屋的缴费记录，这种情况代表缴费日期初始化之后的房屋新建
	            initializeChargeRecord(paymentDate, house);
	            return;
	        }
	        HpsHeatingChargeRecord unchargedRecord = null;
	        for (HpsHeatingChargeRecord record : chargeRecords) {
	            if (record.getChargeState() == ChargeStateEnum.UNCHARGED) {
	                unchargedRecord = record;
	                break;
	            }
	        }
	        if (unchargedRecord != null) {
	            // 如果查到了未缴费的记录，修改房主为可能发生了更名之后的房主
	            unchargedRecord.setHouseOwner(house.getOwner());
	            unchargedRecord.setWageNum(house.getOwner().getWageNum());
	        } else {
	            // 没查到未缴费的记录，只能证明已经缴费结束了，不做任何处理
	        }
		}
//		if (currentPaymentDate == null) {
//			// 没有当前年的取暖费收费日期的定义，直接返回，不做任何处理
//			return;
//		}
	}
	
	private void initializeChargeRecord(HpsHeatingPaymentDate paymentDate, HpsHouse house) {
		HpsHeatingChargeRecord newRecord = new HpsHeatingChargeRecord();
		newRecord.setPaymentDate(paymentDate);
		// 缴费日期
		newRecord.setChargeDate(null);
		newRecord.setChargeState(ChargeStateEnum.UNCHARGED);
		newRecord.setHouse(house);
		newRecord.setHouseOwner(house.getOwner());
		// 身份性质优惠(缴费时即使获取并设定)
		newRecord.setShenfenXingzhiPreferential(null);
		// 缴纳日期优惠(缴费时即使获取并设定)
		newRecord.setPaymentDatePreferential(null);
		// 结转金额
		newRecord.setDivertedCharge(0d);
		// 应该收取的取暖费金额
		newRecord.setMustHeatingCharge(0d);
		// 应该缴纳的滞纳金
		newRecord.setMustZhinajin(0d);
		newRecord.setExpiredDays(0);
		// 免缴滞纳金
		newRecord.setZhinajinOn(false);
		// 收取金额平衡
		newRecord.setBalanced(true);
		// 是否结转
		newRecord.setDiverted(false);
		newRecord.setMustSumCharge(0d);
		newRecord.setActualSumCharge(0d);
		newRecord.setNormalHeatingCharge(0d);
		newRecord.setPreferential(0d);
		newRecord.setWageNum(house.getOwner().getWageNum());
		chargeDao.save(newRecord);
	}
	
	private void initializeChargeRecords(HpsHeatingPaymentDate paymentDate) {
		HpsBase base = paymentDate.getBase();
		HouseQueryParam houseQueryParam = new HouseQueryParam();
		houseQueryParam.setRows(Integer.MAX_VALUE);
		houseQueryParam.setBaseCode(base.getCode());
		List<HpsHouse> houses = houseManager.getHouses(houseQueryParam).getRows();
		for (HpsHouse house : houses) {
			initializeChargeRecord(paymentDate, house);
		}
	}

	@Autowired
	public void setHouseManager(HpsHouseManager houseManager) {
		this.houseManager = houseManager;
	}

	@Override
	@Transactional(readOnly = true)
	public HpsHeatingChargeRecord getChargeRecord(Long recordId) {
		HpsHeatingChargeRecord record = chargeDao.get(recordId);
		List<HpsHeatingChargeRecord> records = new ArrayList<HpsHeatingChargeRecord>();
		records.add(record);
		setRecordProperties(records);
		return record;
	}
	
	private void setRecordProperties(List<HpsHeatingChargeRecord> records) {
		// 分组
		Map<HpsHeatingPaymentDate, List<HpsHeatingChargeRecord>> paymentDateRecordsMap =
					new HashMap<HpsHeatingPaymentDate, List<HpsHeatingChargeRecord>>();
		for (HpsHeatingChargeRecord record : records) {
			if (record.getChargeState() == ChargeStateEnum.CHARGED || 
					record.getChargeState() == ChargeStateEnum.CANCELLED ) {
				// 已缴费的不需要设置单价，优惠等属性，因为这些属性在缴费时候已经保存在缴费记录中
				// 获取结转详细信息
				continue;
			}
			record.setOperUser(SecurityUtil.getCurrentUser());
			HpsHeatingPaymentDate paymentDate = record.getPaymentDate();
			List<HpsHeatingChargeRecord> paymentDateRecords = paymentDateRecordsMap.get(paymentDate);
			if (paymentDateRecords == null) {
				paymentDateRecords = new ArrayList<HpsHeatingChargeRecord>();
				paymentDateRecordsMap.put(paymentDate, paymentDateRecords);
			}
			paymentDateRecords.add(record);
		}
		for (Entry<HpsHeatingPaymentDate, List<HpsHeatingChargeRecord>> paymentDateEntry
				: paymentDateRecordsMap.entrySet()) {
			HpsHeatingPaymentDate paymentDate = paymentDateEntry.getKey();
			List<HpsHeatingChargeRecord> paymentRecords = paymentDateEntry.getValue();
			// 单价
			List<HpsHeatingUnit> units = chargeDao.getHeatingUnit(paymentDate);
			Map<HpsDictItem, HpsHeatingUnit> unitsMap = new HashMap<HpsDictItem, HpsHeatingUnit>();
			for (HpsHeatingUnit unit : units) {
				unitsMap.put(unit.getYongfangXingzhi(), unit);
			}
			// 身份性质优惠
			List<HpsHeatingShenfenXingzhiPreferential> sfPreferentials = chargeDao.getShenfenXingzhiPreferential(paymentDate);
			Map<HpsDictItem, HpsHeatingShenfenXingzhiPreferential> sfMap = new HashMap<HpsDictItem, HpsHeatingShenfenXingzhiPreferential>();
			for (HpsHeatingShenfenXingzhiPreferential sf : sfPreferentials) {
				sfMap.put(sf.getShenfengXingzhi(), sf);
			}
			// 缴费日期优惠
			List<HpsHeatingPaymentDatePreferential> datePreferentials = chargeDao.getPaymentDatePreferential(paymentDate);
			HpsHeatingPaymentDatePreferential datePreferentail = null;
			Date currentDate = DateUtils.truncate(new Date(), Calendar.DATE);
			for (HpsHeatingPaymentDatePreferential date : datePreferentials) {
				Date startDate = date.getStartDate();
				startDate = DateUtils.truncate(startDate, Calendar.DATE);
				Date endDate = date.getEndDate();
				endDate = DateUtils.truncate(endDate, Calendar.DATE);
				if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
					datePreferentail = date;
					break;
				}
			}
			for (HpsHeatingChargeRecord record : paymentRecords) {
				// 单价
				HpsHeatingUnit unit = unitsMap.get(record.getHouse().getYongfangXingzhi());
				record.setUnit(unit);
				// 身份性质优惠
				HpsHeatingShenfenXingzhiPreferential sfP = sfMap.get(record.getHouse().getShenfenXingzhi());
				record.setShenfenXingzhiPreferential(sfP);
				// 缴费日期优惠
				record.setPaymentDatePreferential(datePreferentail);
			}
		}
	}

	@Override
	@Transactional
	public HpsHeatingChargeRecord charge(Long recordId, 
			Double actualCharge,
			boolean zhinajinOn,
			boolean stopped,
			boolean livingSohard,
			String wageNum,
			String remarks) {
		HpsHeatingChargeRecord record = chargeDao.get(recordId);
		if (record.getChargeState() != ChargeStateEnum.UNCHARGED) {
			// 只有未缴费的才能缴费
			return record;
		}
		record.setActualSumCharge(actualCharge);
		record.setZhinajinOn(zhinajinOn);
		record.setStopped(stopped);
		record.setWageNum(wageNum);
		record.setLivingSohard(livingSohard);
		record.setRemarks(remarks);
		// 设置单价等属性
		List<HpsHeatingChargeRecord> records = new ArrayList<HpsHeatingChargeRecord>();
		records.add(record);
		setRecordProperties(records);
		Date nowDate = new Date();
		record.setChargeDate(nowDate);
		// 应缴纳
		double mustSum = record.getMustSumCharge();
		if (mustSum > actualCharge) {
			record.setBalanced(false);
		}
		// 结转处理
		Double divertedCharge = 0d;
		List<HpsHeatingChargeRecord> divertedRecords = getMustDivertChargeRecords(record);
		for (HpsHeatingChargeRecord divertedRecord : divertedRecords) {
			double mustHeatingCharge = divertedRecord.getMustHeatingCharge();
			divertedRecord.setChargeDate(nowDate);
			divertedRecord.setChargeState(ChargeStateEnum.CHARGED);
			divertedRecord.setDiverted(true);
			divertedRecord.setDivertToRecordId(recordId);
			chargeDao.save(divertedRecord);
			divertedCharge += mustHeatingCharge;
		}
		record.setDivertedCharge(divertedCharge);
		record.setMustHeatingCharge(record.getMustHeatingCharge());
		record.setMustSumCharge(record.getMustSumCharge());
		record.setActualSumCharge(actualCharge);
		record.setPreferential(record.getPreferential());
		record.setMustZhinajin(record.getMustZhinajin());
		record.setNormalHeatingCharge(record.getNormalHeatingCharge());
		record.setExpiredDays(record.getExpiredDays());
		record.setOperUser(SecurityUtil.getCurrentUser());
		record.setChargeState(ChargeStateEnum.CHARGED);
		chargeDao.save(record);
		return record;
	}

	@Override
	@Transactional
	public HpsHeatingChargeRecord cancel(Long recordId, String remarks) {
		HpsHeatingChargeRecord record = chargeDao.get(recordId);
		if (record.getChargeState() != ChargeStateEnum.CHARGED) {
			// 只有缴费的才能取消
			return record;
		}
		HpsHeatingChargeRecord unchargedNewRecord = null;
		try {
			unchargedNewRecord = (HpsHeatingChargeRecord) BeanUtils.cloneBean(record);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		unchargedNewRecord.setId(null);
		unchargedNewRecord.setChargeState(ChargeStateEnum.UNCHARGED);
		chargeDao.save(unchargedNewRecord);
		
		record.setChargeState(ChargeStateEnum.CANCELLED);
		record.setCancelledCause(remarks);
		chargeDao.save(record);
		// 结转处理
		List<HpsHeatingChargeRecord> divertedRecords = getDivertedChargeRecords(recordId);
		for (HpsHeatingChargeRecord divertedRecord : divertedRecords) {
			divertedRecord.setDiverted(false);
			divertedRecord.setChargeState(ChargeStateEnum.UNCHARGED);
			divertedRecord.setDivertToRecordId(null);
			chargeDao.save(divertedRecord);
		}
		return record;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HpsHeatingChargeRecord> getDivertedChargeRecords(Long recordId) {
		return chargeDao.getDivertedChargeRecords(recordId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HpsHeatingChargeRecord> getMustDivertChargeRecords(
			HpsHeatingChargeRecord record) {
		List<HpsHeatingChargeRecord> records = chargeDao.getMustDivertChargeRecords(record);
		setRecordProperties(records);
		return records;
		
	}

}
