package org.appfuse.service.hps;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.ElectricUnitQueryParam;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsElectricUnitManager extends GenericManager<HpsElectricUnit, Long> {

	PaginationResult<HpsElectricUnit> getUnits(ElectricUnitQueryParam param);
}
