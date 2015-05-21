package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsWeixiufeiPaymentDateDao;
import org.appfuse.service.hps.HpsWeixiufeiPaymentDateManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.exception.PaymentDateExistsException;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
@Service
public class HpsWeixiufeiPaymentDateManagerImpl extends GenericManagerImpl<HpsMaintainPaymentDate, Long> implements HpsWeixiufeiPaymentDateManager {

	private HpsWeixiufeiPaymentDateDao weixiufeiPaymentDateDao;
	
	@Override
	@Transactional
	public HpsMaintainPaymentDate addPaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate) {
		
		// 判断数据库中是否存在
		List<HpsMaintainPaymentDate> weixiufeiPaymentDateList = 
				weixiufeiPaymentDateDao.getPaymentDateByBase(
						weixiufeiPaymentDate.getBase().getId(), 
						weixiufeiPaymentDate.getTitle());
		if (weixiufeiPaymentDateList.size() != 0) {
			throw new PaymentDateExistsException(weixiufeiPaymentDate.getTitle()  + "' 已经存在!");
		}
		
		return weixiufeiPaymentDateDao.addPaymentDate(weixiufeiPaymentDate);
	}
	
	@Override
	@Transactional
	public HpsMaintainPaymentDate updatePaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate) {
		HpsMaintainPaymentDate paymentDateInDB = weixiufeiPaymentDateDao
				.getPaymentDateById(weixiufeiPaymentDate.getId()).get(0);
		paymentDateInDB.setTitle(weixiufeiPaymentDate.getTitle());
		paymentDateInDB.setPayStartDate(weixiufeiPaymentDate.getPayStartDate());
		paymentDateInDB.setPayEndDate(weixiufeiPaymentDate.getPayEndDate());
//		paymentDateInDB.setChargeRecordsInitialized(weixiufeiPaymentDate.isChargeRecordsInitialized());
		return weixiufeiPaymentDateDao.addPaymentDate(paymentDateInDB);
	}
	
	@Override
	@Transactional
	public List<HpsMaintainPaymentDate> getPaymentDate(Long baseId) {
		return weixiufeiPaymentDateDao.getPaymentDate(baseId);
	}
	
	@Override
	@Transactional
	public List<HpsMaintainPaymentDate> getPaymentDateById(Long id) {
		return weixiufeiPaymentDateDao.getPaymentDateById(id);
	}

	@Autowired
	public void setWeixiufeiPaymentDateDao(
			HpsWeixiufeiPaymentDateDao weixiufeiPaymentDateDao) {
		this.weixiufeiPaymentDateDao = weixiufeiPaymentDateDao;
	}
	
}
