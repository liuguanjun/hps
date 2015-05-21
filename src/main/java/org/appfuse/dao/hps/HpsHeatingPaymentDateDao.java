package org.appfuse.dao.hps;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsHeatingPaymentDate;

public interface HpsHeatingPaymentDateDao extends GenericDao<HpsHeatingPaymentDate, Long> {
	HpsHeatingPaymentDate addPaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate);
	List getPaymentDateByBaseAndDate(Long baseId, String title, Date startDate, Date endDate);
	List<HpsHeatingPaymentDate> getPaymentDateAndZhinajin(Long baseId);
	List<HpsHeatingPaymentDate> getPaymentDateAndZhinajinById(Long id);
}
