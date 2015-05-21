package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.WeixiufeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsMaintainUnit;

public interface HpsWeixiufeiUnitManager extends GenericManager<HpsMaintainUnit, Long> {
	HpsMaintainUnit addWeixiufeiUnit(HpsMaintainUnit heatingUnit);
	HpsMaintainUnit updateWeixiufeiUnit(HpsMaintainUnit heatingUnit);
	List<HpsMaintainUnit> getWeixiufeiUnit(WeixiufeiDiscountQueryParam param);
	List<HpsMaintainUnit> getWeixiufeiUnitById(Long id);
}

