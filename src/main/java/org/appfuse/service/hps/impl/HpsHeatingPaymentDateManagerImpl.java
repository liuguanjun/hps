package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsHeatingPaymentDateDao;
import org.appfuse.service.hps.HpsHeatingPaymentDateManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.exception.PaymentDateExistsException;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
@Service
public class HpsHeatingPaymentDateManagerImpl extends GenericManagerImpl<HpsHeatingPaymentDate, Long> implements HpsHeatingPaymentDateManager {

	private HpsHeatingPaymentDateDao heatingPaymentDateDao;
	
	@Override
	@Transactional
	public HpsHeatingPaymentDate addPaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate) {
		
		// 判断数据库中是否存在
		List<HpsHeatingPaymentDate> heatingPaymentDateList = 
				heatingPaymentDateDao.getPaymentDateByBaseAndDate(
						heatingPaymentDate.getBase().getId(), 
						heatingPaymentDate.getTitle(),
						heatingPaymentDate.getPayStartDate(), 
						heatingPaymentDate.getPayEndDate());
		if (heatingPaymentDateList.size() != 0) {
			throw new PaymentDateExistsException(heatingPaymentDate.getTitle()  + "' 已经存在!");
		}
		
		return heatingPaymentDateDao.addPaymentDateAndZhinajin(heatingPaymentDate);
	}
	
	@Override
	@Transactional
	public HpsHeatingPaymentDate updatePaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate) {
		HpsHeatingPaymentDate paymentDateInDB = heatingPaymentDateDao.get(heatingPaymentDate.getId());
//		paymentDateInDB.setChargeRecordsInitialized(heatingPaymentDate.isChargeRecordsInitialized());
		paymentDateInDB.setTitle(heatingPaymentDate.getTitle());
		paymentDateInDB.setPayStartDate(heatingPaymentDate.getPayStartDate());
		paymentDateInDB.setPayEndDate(heatingPaymentDate.getPayEndDate());
		paymentDateInDB.setZhinajinRate(heatingPaymentDate.getZhinajinRate());
		paymentDateInDB.setStopHeatingRate(heatingPaymentDate.getStopHeatingRate());
		paymentDateInDB.setLivingSoHardRate(heatingPaymentDate.getLivingSoHardRate());
		return heatingPaymentDateDao.addPaymentDateAndZhinajin(paymentDateInDB);
	}
	
	@Override
	@Transactional
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajin(Long baseId) {
		return heatingPaymentDateDao.getPaymentDateAndZhinajin(baseId);
	}
	
	@Override
	@Transactional
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajinById(Long id) {
		return heatingPaymentDateDao.getPaymentDateAndZhinajinById(id);
	}

	@Autowired
	public void setHeatingPaymentDateDao(
			HpsHeatingPaymentDateDao heatingPaymentDateDao) {
		this.heatingPaymentDateDao = heatingPaymentDateDao;
	}
	
}
