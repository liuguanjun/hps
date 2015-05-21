package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.ElectricChaobiaoQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoPaginationResult;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricPaymentDate;

public interface HpsElectricChaobiaoManager extends GenericManager<HpsElectricChaobiao, Long> {
	
    ElectricChaobiaoPaginationResult getChaobiaos(ElectricChaobiaoQueryParam param);
	
	HpsElectricPaymentDate getElectricPaymentDate(Long paymentDateId);
	List<HpsElectricChaobiao> updateElectricChaobiao(List<HpsElectricChaobiao> electricityChaobiao);

	void initializeChaobiaoRecords();

	List<HpsElectricChaobiao> getChaobiaos(Long paymentDateId);
}
