package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.model.HpsHeatingPaymentDate;

public interface HpsHeatingPaymentDateManager  extends GenericManager<HpsHeatingPaymentDate, Long> {
	HpsHeatingPaymentDate addPaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate);
	HpsHeatingPaymentDate updatePaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate);
	List<HpsHeatingPaymentDate> getPaymentDateAndZhinajin(Long baseId);
	List<HpsHeatingPaymentDate> getPaymentDateAndZhinajinById(Long id);
}
