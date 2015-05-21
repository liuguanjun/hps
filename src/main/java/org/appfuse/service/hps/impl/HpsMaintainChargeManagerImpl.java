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
import org.appfuse.dao.hps.HpsMaintainChargeDao;
import org.appfuse.service.hps.HpsHouseManager;
import org.appfuse.service.hps.HpsMaintainChargeManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.controller.queryparam.MaintainChargeQueryParam;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.HpsMaintainUnit;
import com.my.hps.webapp.model.MaintainChargeRecordPaginationResult;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;
import com.my.hps.webapp.util.SecurityUtil;

@Service
public class HpsMaintainChargeManagerImpl extends GenericManagerImpl<HpsHeatingChargeRecord, Long>
		implements HpsMaintainChargeManager {
	
	private HpsMaintainChargeDao chargeDao;
	private HpsHouseManager houseManager;

	@Override
	@Transactional(readOnly = true)
	public List<HpsMaintainPaymentDate> getPaymentDates(String baseCode) {
		List<HpsMaintainPaymentDate> paymentDates = chargeDao.getPaymentDates(baseCode);
		return paymentDates;
	}
	
	@Override
	@Transactional(readOnly = true)
	public MaintainChargeRecordPaginationResult getChargeRecords(MaintainChargeQueryParam queryParam) {
	    MaintainChargeRecordPaginationResult result = chargeDao.getChargeRecords(queryParam);
		List<HpsMaintainChargeRecord> records = result.getRows();
		setRecordProperties(records);
		return result;
	}

	@Autowired
	public void setChargeDao(HpsMaintainChargeDao chargeDao) {
		this.chargeDao = chargeDao;
	}

	@Override
	@Transactional
	public void initializeChargeRecords() {
		List<HpsMaintainPaymentDate> paymentDates = chargeDao.getPaymentDates();
		for (HpsMaintainPaymentDate paymentDate : paymentDates) {
			if (paymentDate.isChargeRecordsInitialized()) {
				// 已经初始过了
				continue;
			}
			initializeChargeRecords(paymentDate);
			paymentDate.setChargeRecordsInitialized(true);
		}
	}
	
	@Override
	@Transactional
	public void initializeChargeRecordCurrentYear(HpsHouse house) {
		//Calendar currentCalendar = new GregorianCalendar();
		//int currentYear = currentCalendar.get(Calendar.YEAR);
		List<HpsMaintainPaymentDate> paymentDates = getPaymentDates(house.getLouzuo()
				.getArea().getBase().getCode());
		//HpsMaintainPaymentDate currentPaymentDate = null;
		for (HpsMaintainPaymentDate paymentDate : paymentDates) {
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
		    List<HpsMaintainChargeRecord> chargeRecords = chargeDao.getChargeRecords(
		            paymentDate.getId(), house.getId());
		    if (chargeRecords.size() == 0) { 
		        // 缴费日期已经初始化，但是仍然没有关于此房屋的缴费记录，这种情况代表缴费日期初始化之后的房屋新建
		        initializeChargeRecord(paymentDate, house);
		        return;
		    }
		    HpsMaintainChargeRecord unchargedRecord = null;
		    for (HpsMaintainChargeRecord record : chargeRecords) {
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
	
	private void initializeChargeRecord(HpsMaintainPaymentDate paymentDate, HpsHouse house) {
		HpsMaintainChargeRecord newRecord = new HpsMaintainChargeRecord();
		newRecord.setPaymentDate(paymentDate);
		// 缴费日期
		newRecord.setChargeDate(null);
		newRecord.setChargeState(ChargeStateEnum.UNCHARGED);
		newRecord.setHouse(house);
		newRecord.setHouseOwner(house.getOwner());
		// 结转金额
		newRecord.setDivertedCharge(0d);
		// 收取金额平衡
		newRecord.setBalanced(true);
		// 是否结转
		newRecord.setDiverted(false);
		newRecord.setMustCharge(0d);
		newRecord.setActualCharge(0d);
		newRecord.setGratis(false);
		newRecord.setMonthCount(12);
		newRecord.setWageNum(house.getOwner().getWageNum());
		chargeDao.save(newRecord);
	}
	
	private void initializeChargeRecords(HpsMaintainPaymentDate paymentDate) {
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
	public HpsMaintainChargeRecord getChargeRecord(Long recordId) {
		HpsMaintainChargeRecord record = chargeDao.get(recordId);
		List<HpsMaintainChargeRecord> records = new ArrayList<HpsMaintainChargeRecord>();
		records.add(record);
		setRecordProperties(records);
		return record;
	}
	
	private void setRecordProperties(List<HpsMaintainChargeRecord> records) {
		// 分组
		Map<HpsMaintainPaymentDate, List<HpsMaintainChargeRecord>> paymentDateRecordsMap =
					new HashMap<HpsMaintainPaymentDate, List<HpsMaintainChargeRecord>>();
		for (HpsMaintainChargeRecord record : records) {
			if (record.getChargeState() == ChargeStateEnum.CHARGED || 
					record.getChargeState() == ChargeStateEnum.CANCELLED ) {
				// 已缴费的不需要设置单价，优惠等属性，因为这些属性在缴费时候已经保存在缴费记录中
				// 获取结转详细信息
				continue;
			}
			record.setOperUser(SecurityUtil.getCurrentUser());
			HpsMaintainPaymentDate paymentDate = record.getPaymentDate();
			List<HpsMaintainChargeRecord> paymentDateRecords = paymentDateRecordsMap.get(paymentDate);
			if (paymentDateRecords == null) {
				paymentDateRecords = new ArrayList<HpsMaintainChargeRecord>();
				paymentDateRecordsMap.put(paymentDate, paymentDateRecords);
			}
			paymentDateRecords.add(record);
		}
		for (Entry<HpsMaintainPaymentDate, List<HpsMaintainChargeRecord>> paymentDateEntry
				: paymentDateRecordsMap.entrySet()) {
			HpsMaintainPaymentDate paymentDate = paymentDateEntry.getKey();
			List<HpsMaintainChargeRecord> paymentRecords = paymentDateEntry.getValue();
			// 单价
			List<HpsMaintainUnit> units = chargeDao.getMaintainUnit(paymentDate);
			Map<HpsDictItem, HpsMaintainUnit> unitsMap = new HashMap<HpsDictItem, HpsMaintainUnit>();
			for (HpsMaintainUnit unit : units) {
				unitsMap.put(unit.getYongfangXingzhi(), unit);
			}
			for (HpsMaintainChargeRecord record : paymentRecords) {
				// 单价
				HpsMaintainUnit unit = unitsMap.get(record.getHouse().getYongfangXingzhi());
				record.setUnit(unit);
			}
		}
	}

	@Override
	@Transactional
	public HpsMaintainChargeRecord charge(Long recordId, 
			Double actualCharge,
			int monthCount,
			boolean gratis,
			String wageNum,
			String remarks) {
		HpsMaintainChargeRecord record = chargeDao.get(recordId);
		if (record.getChargeState() != ChargeStateEnum.UNCHARGED) {
			// 只有未缴费的才能缴费
			return record;
		}
		record.setActualCharge(actualCharge);
		record.setGratis(gratis);
		record.setWageNum(wageNum);
		record.setRemarks(remarks);
		record.setMonthCount(monthCount);
		// 设置单价等属性
		List<HpsMaintainChargeRecord> records = new ArrayList<HpsMaintainChargeRecord>();
		records.add(record);
		setRecordProperties(records);
		Date nowDate = new Date();
		record.setChargeDate(nowDate);
		// 应缴纳
		double mustSum = record.getMustCharge();
		if (mustSum > actualCharge) {
			record.setBalanced(false);
		}
		// 结转处理
		Double divertedCharge = 0d;
		List<HpsMaintainChargeRecord> divertedRecords = getMustDivertChargeRecords(record);
		for (HpsMaintainChargeRecord divertedRecord : divertedRecords) {
			double mustHeatingCharge = divertedRecord.getMustCharge();
			divertedRecord.setDiverted(true);
			divertedRecord.setChargeDate(nowDate);
			divertedRecord.setChargeState(ChargeStateEnum.CHARGED);
			divertedRecord.setDivertToRecordId(recordId);
			chargeDao.save(divertedRecord);
			divertedCharge += mustHeatingCharge;
		}
		record.setDivertedCharge(divertedCharge);
		record.setMustCharge(record.getMustCharge());
		record.setActualCharge(actualCharge);
		record.setOperUser(SecurityUtil.getCurrentUser());
		record.setChargeState(ChargeStateEnum.CHARGED);
		chargeDao.save(record);
		return record;
	}

	@Override
	@Transactional
	public HpsMaintainChargeRecord cancel(Long recordId, String remarks) {
		HpsMaintainChargeRecord record = chargeDao.get(recordId);
		if (record.getChargeState() != ChargeStateEnum.CHARGED) {
			// 只有缴费的才能取消
			return record;
		}
		HpsMaintainChargeRecord unchargedNewRecord = null;
		try {
			unchargedNewRecord = (HpsMaintainChargeRecord) BeanUtils.cloneBean(record);
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
		List<HpsMaintainChargeRecord> divertedRecords = getDivertedChargeRecords(recordId);
		for (HpsMaintainChargeRecord divertedRecord : divertedRecords) {
			divertedRecord.setDiverted(false);
			divertedRecord.setDivertToRecordId(null);
			divertedRecord.setChargeState(ChargeStateEnum.UNCHARGED);
			chargeDao.save(divertedRecord);
		}
		return record;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HpsMaintainChargeRecord> getDivertedChargeRecords(Long recordId) {
		return chargeDao.getDivertedChargeRecords(recordId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HpsMaintainChargeRecord> getMustDivertChargeRecords(
			HpsMaintainChargeRecord record) {
		List<HpsMaintainChargeRecord> records = chargeDao.getMustDivertChargeRecords(record);
		setRecordProperties(records);
		return records;
		
	}

}
