package org.appfuse.dao.hps;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.ElectricUnitQueryParam;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsElectricUnitDao extends GenericDao<HpsElectricUnit, Long> {

	PaginationResult<HpsElectricUnit> getUnits(ElectricUnitQueryParam param);
}
