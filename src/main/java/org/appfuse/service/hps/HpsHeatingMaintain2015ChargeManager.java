package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsHeatingMaintainChargeRecord2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;
import com.my.hps.webapp.model.HpsHouse;

public interface HpsHeatingMaintain2015ChargeManager extends GenericManager<HpsHeatingMaintainChargeRecord2015, Long> {
    
    HpsHeatingMaintainPaymentDate2015 getPaymentDate(Long paymentDateId);
    
    HpsHeatingMaintainPaymentDate2015 savePaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate);
	
	List<HpsHeatingMaintainPaymentDate2015> getPaymentDates();
	
	HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(HeatingMaintainCharge2015QueryParam param);
	
	HpsHeatingMaintainChargeRecord2015 getChargeRecord(Long recordId);
	
//	HpsHeatingMaintainChargeRecord2015 getChargeRecordByHouseId(Long houseId);
	
	void initializeChargeRecords();
	
	void initializeChargeRecord(HpsHouse house);
	
	HpsHeatingMaintainChargeRecord2015 charge(Long recordId, 
			Double actualCharge,
			String wageNum,
			String remarks);
	
	HpsHeatingMaintainChargeRecord2015 cancel(Long recordId, String remarks);

}
