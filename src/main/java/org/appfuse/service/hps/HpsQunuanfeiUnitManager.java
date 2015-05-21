package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingUnit;

public interface HpsQunuanfeiUnitManager   extends GenericManager<HpsHeatingUnit, Long> {
	HpsHeatingUnit addQunuanfeiUnit(HpsHeatingUnit heatingUnit);
	HpsHeatingUnit updateQunuanfeiUnit(HpsHeatingUnit heatingUnit);
	List<HpsHeatingUnit> getQunuanfeiUnit(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingUnit> getQunuanfeiUnitById(Long id);
}
