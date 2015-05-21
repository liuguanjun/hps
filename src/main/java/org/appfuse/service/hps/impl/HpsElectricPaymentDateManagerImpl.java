package org.appfuse.service.hps.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.appfuse.dao.hps.HpsBaseDao;
import org.appfuse.dao.hps.HpsElectricPaymentDateDao;
import org.appfuse.service.hps.HpsElectricPaymentDateManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsElectricPaymentDate;

@Service
public class HpsElectricPaymentDateManagerImpl 
		extends GenericManagerImpl<HpsElectricPaymentDate, Long> implements
		HpsElectricPaymentDateManager {
	
	private static final long BASE_KOUQIAN_ID = 1l;
	private static final long BASE_LVSHUN_ID = 4l;
	
	private HpsElectricPaymentDateDao paymentDateDao;
	private HpsBaseDao baseDao;

	@Override
	@Transactional(readOnly = true)
	public List<HpsElectricPaymentDate> getPaymentDatesByNow(String baseCode) {
		return paymentDateDao.getPaymentDatesByNow(baseCode);
	}

	@Autowired
	public void setPaymentDateDao(HpsElectricPaymentDateDao paymentDateDao) {
		this.dao = paymentDateDao;
		this.paymentDateDao = paymentDateDao;
	}
	
	@Override
	@Transactional
	public void initializePymentDates() {
		HpsBase kouqianBase = baseDao.get(BASE_KOUQIAN_ID);
		HpsBase lvshunBase = baseDao.get(BASE_LVSHUN_ID);
		initializePymentDates(kouqianBase);
		initializePymentDates(lvshunBase);
	}
	
	@Autowired
	public void setBaseDao(HpsBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	private void initializePymentDates(HpsBase base) {
		String baseCode = base.getCode();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
		List<String> currentYearMonthStrs= new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			currentYearMonthStrs.add(year * 100 + i + "");
		}
		List<HpsElectricPaymentDate> kouqianPaymentDates = paymentDateDao.getPaymentDates(baseCode, year);
		List<String> currentYearMonthStrsInDB = new ArrayList<String>();
		for (HpsElectricPaymentDate paymentDate : kouqianPaymentDates) {
			currentYearMonthStrsInDB.add(monthFormat.format(paymentDate.getMonth()));
		}
		int i = 0;
		for (String currentYearMonthStr : currentYearMonthStrs) {
			i++;
			if (!currentYearMonthStrsInDB.contains(currentYearMonthStr)) {
				HpsElectricPaymentDate newPaymentDate = new HpsElectricPaymentDate();
				newPaymentDate.setBase(base);
				try {
					newPaymentDate.setMonth(monthFormat.parse(currentYearMonthStr));
					int shouquMonth = year * 100 + i + 1;
					newPaymentDate.setStartDate(dayFormat.parse(shouquMonth + "20"));
					int nextMonth;
					if (i >= 11) {
						nextMonth = (shouquMonth / 100 + 1) * 100 + (i % 10);
					} else {
						nextMonth = shouquMonth + 1;
					}
					newPaymentDate.setEndDate(dayFormat.parse(nextMonth + "05"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				newPaymentDate.setChaobiaosInitialized(false);
				paymentDateDao.save(newPaymentDate);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<HpsElectricPaymentDate> getPaymentDates(String baseCode) {
		return paymentDateDao.getPaymentDates(baseCode);
	}
	

}
