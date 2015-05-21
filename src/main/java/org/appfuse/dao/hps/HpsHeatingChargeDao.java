package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.model.HeatingChargeRecordPaginationResult;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;

public interface HpsHeatingChargeDao extends GenericDao<HpsHeatingChargeRecord, Long> {
	
	List<HpsHeatingPaymentDate> getPaymentDates(String baseCode);
	
	List<HpsHeatingPaymentDate> getPaymentDates();
	
	HpsHeatingPaymentDate getPaymentDate(Long paymentDateId);
	
	HeatingChargeRecordPaginationResult getChargeRecords(HeatingChargeQueryParam param);
	
	List<HpsHeatingUnit> getHeatingUnit(HpsHeatingPaymentDate paymentDate);
	
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenXingzhiPreferential(HpsHeatingPaymentDate paymentDate);
	
	List<HpsHeatingPaymentDatePreferential> getPaymentDatePreferential(HpsHeatingPaymentDate paymentDate);
	
	List<HpsHeatingChargeRecord> getDivertedChargeRecords(Long recordId);
	
	List<HpsHeatingChargeRecord> getMustDivertChargeRecords(
			HpsHeatingChargeRecord record);
	
	List<HpsHeatingChargeRecord> getChargeRecords(Long paymentDateId, Long houseId);

}
