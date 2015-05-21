package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsElectricPaymentDate;

public interface HpsElectricPaymentDateDao extends GenericDao<HpsElectricPaymentDate, Long> {
	
	List<HpsElectricPaymentDate> getPaymentDatesByNow(String baseCode);
	
	List<HpsElectricPaymentDate> getPaymentDates(String baseCode, int year);

	List<HpsElectricPaymentDate> getPaymentDates(String baseCode);
	
}
