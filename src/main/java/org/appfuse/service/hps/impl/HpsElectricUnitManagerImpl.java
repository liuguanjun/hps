package org.appfuse.service.hps.impl;

import org.appfuse.dao.hps.HpsElectricUnitDao;
import org.appfuse.service.hps.HpsElectricUnitManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.ElectricUnitQueryParam;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.PaginationResult;

@Service
public class HpsElectricUnitManagerImpl extends GenericManagerImpl<HpsElectricUnit, Long> 
	implements HpsElectricUnitManager {
	
	private HpsElectricUnitDao unitDao;

	@Override
	@Transactional(readOnly = true)
	public PaginationResult<HpsElectricUnit> getUnits(
			ElectricUnitQueryParam param) {
		PaginationResult<HpsElectricUnit> result = unitDao.getUnits(param);
		return result;
	}

	@Autowired
	public void setUnitDao(HpsElectricUnitDao unitDao) {
		this.dao = unitDao;
		this.unitDao = unitDao;
	}

}
