package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.MaintainChargeQueryParam;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.MaintainChargeRecordPaginationResult;

public interface HpsMaintainChargeManager extends GenericManager<HpsHeatingChargeRecord, Long> {
	
	List<HpsMaintainPaymentDate> getPaymentDates(String baseCode);
	
	MaintainChargeRecordPaginationResult getChargeRecords(MaintainChargeQueryParam param);
	
	List<HpsMaintainChargeRecord> getDivertedChargeRecords(Long recordId);
	
	HpsMaintainChargeRecord getChargeRecord(Long recordId);
	
	void initializeChargeRecords();
	
	void initializeChargeRecordCurrentYear(HpsHouse house);
	
	HpsMaintainChargeRecord charge(Long recordId, 
			Double actualCharge,
			int monthCount,
			boolean gratis,
			String wageNum,
			String remarks);
	
	HpsMaintainChargeRecord cancel(Long recordId, String remarks);

	List<HpsMaintainChargeRecord> getMustDivertChargeRecords(
			HpsMaintainChargeRecord record);

}
