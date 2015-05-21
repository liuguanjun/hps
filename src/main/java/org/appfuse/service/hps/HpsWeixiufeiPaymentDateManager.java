package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.model.HpsMaintainPaymentDate;

public interface HpsWeixiufeiPaymentDateManager extends GenericManager<HpsMaintainPaymentDate, Long> {
	HpsMaintainPaymentDate addPaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate);
	HpsMaintainPaymentDate updatePaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate);
	List<HpsMaintainPaymentDate> getPaymentDate(Long baseId);
	List<HpsMaintainPaymentDate> getPaymentDateById(Long id);
}
