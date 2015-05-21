package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.ElectricChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.PaginationQueryParam;
import com.my.hps.webapp.controller.vo.HpsElectricUserTongjiRowView;
import com.my.hps.webapp.model.ElectricChargeState;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.PaginationResult;

/**
 * 电费缴费的Manager
 * 
 * @author liuguanjun
 *
 */
public interface HpsElectricChargeManager extends GenericManager<HpsElectricChargeRecord, Long> {
	
	PaginationResult<ElectricChargeState> getChargeStates(ElectricChargeQueryParam queryParam);
	
	ElectricChargeState getChargeState(Long houseId, boolean zhinajinOn);
	
	HpsElectricChargeRecord charge(Long houseId, Double chargeValue, boolean zhinajinOn);
	
	/**
	 * 计算抄表记录应缴纳的费用
	 * 
	 * @param chaobiao
	 */
//	void caculateChaobiaoCharge(HpsElectricChaobiao chaobiao);
	
	/**
	 * 取消结算
	 * 
	 * @param recordId
	 * @param cause
	 * @return
	 */
	HpsElectricChargeRecord cancelChargeRecord(Long recordId, String cause);

	PaginationResult<HpsElectricChargeRecord> getChargeRecords(Long houseId, PaginationQueryParam param);
	
	PaginationResult<HpsElectricChargeRecord> getChargeRecords(ElectricTongjiQueryParam param);

	List<HpsElectricUserTongjiRowView> getUserTongjiRowList(ElectricTongjiQueryParam param);

	HpsElectricChargeRecord initHouseSurplus(Long houseId, Double initSurplus,
			Integer initElectricReadout);

}
