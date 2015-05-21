package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.model.HeatingChargeRecordPaginationResult;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.HpsHouse;

public interface HpsHeatingChargeManager extends GenericManager<HpsHeatingChargeRecord, Long> {
	
	List<HpsHeatingPaymentDate> getPaymentDates(String baseCode);
	
	HeatingChargeRecordPaginationResult getChargeRecords(HeatingChargeQueryParam param);
	
	List<HpsHeatingChargeRecord> getDivertedChargeRecords(Long recordId);
	
	HpsHeatingChargeRecord getChargeRecord(Long recordId);
	
	void initializeChargeRecords();
	
	HpsHeatingChargeRecord charge(Long recordId, 
			Double actualCharge,
			boolean zhinajinOff,
			boolean stopped,
			boolean livingSohard,
			String wageNum,
			String remarks);
	
	HpsHeatingChargeRecord cancel(Long recordId, String remarks);

	List<HpsHeatingChargeRecord> getMustDivertChargeRecords(
			HpsHeatingChargeRecord record);
	
	void initializeChargeRecordCurrentYear(HpsHouse house);

}
