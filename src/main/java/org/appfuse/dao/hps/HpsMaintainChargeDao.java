package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.MaintainChargeQueryParam;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.HpsMaintainUnit;
import com.my.hps.webapp.model.MaintainChargeRecordPaginationResult;

public interface HpsMaintainChargeDao extends GenericDao<HpsMaintainChargeRecord, Long> {
	
	List<HpsMaintainPaymentDate> getPaymentDates(String baseCode);
	
	List<HpsMaintainPaymentDate> getPaymentDates();
	
	HpsMaintainPaymentDate getPaymentDate(Long paymentDateId);
	
	MaintainChargeRecordPaginationResult getChargeRecords(MaintainChargeQueryParam param);
	
	List<HpsMaintainUnit> getMaintainUnit(HpsMaintainPaymentDate paymentDate);
	
	List<HpsMaintainChargeRecord> getDivertedChargeRecords(Long recordId);
	
	List<HpsMaintainChargeRecord> getMustDivertChargeRecords(
			HpsMaintainChargeRecord record);
	
	List<HpsMaintainChargeRecord> getChargeRecords(Long paymentDateId, Long houseId);

}
