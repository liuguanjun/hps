package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.model.HpsElectricPaymentDate;

public interface HpsElectricPaymentDateManager  extends GenericManager<HpsElectricPaymentDate, Long> {
	
	List<HpsElectricPaymentDate> getPaymentDatesByNow(String baseCode);
	
	List<HpsElectricPaymentDate> getPaymentDates(String baseCode);

	void initializePymentDates();
}
