package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsMaintainPaymentDate;

public interface HpsWeixiufeiPaymentDateDao  extends GenericDao<HpsMaintainPaymentDate, Long> {
	HpsMaintainPaymentDate addPaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate);
	List<HpsMaintainPaymentDate> getPaymentDateByBase(Long baseId, String title);
	List<HpsMaintainPaymentDate> getPaymentDate(Long baseId);
	List<HpsMaintainPaymentDate> getPaymentDateById(Long id);
}

