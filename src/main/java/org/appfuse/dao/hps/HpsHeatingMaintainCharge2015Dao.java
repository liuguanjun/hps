package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsHeatingMaintainChargeRecord2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;

public interface HpsHeatingMaintainCharge2015Dao extends GenericDao<HpsHeatingMaintainChargeRecord2015, Long> {
	
	List<HpsHeatingMaintainPaymentDate2015> getPaymentDates();
	
	HpsHeatingMaintainPaymentDate2015 getPaymentDate(Long paymentDateId);
	
	HpsHeatingMaintainChargeRecord2015 getChargeRecordByHouseId(Long houseId);
	
	HpsHeatingMaintainPaymentDate2015 savePaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate);
	
	HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(HeatingMaintainCharge2015QueryParam param);
	
}
