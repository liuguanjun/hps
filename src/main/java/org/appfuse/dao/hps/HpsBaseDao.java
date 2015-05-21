package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;

public interface HpsBaseDao extends GenericDao<HpsBase, Long> {
	
	HpsBase getBaseByCode(String code);
	HpsBase getBaseById(Long id);
	void deleteBaseTreeElement(Long areaId);
	HpsArea saveHpsArea(HpsArea area);
	List<HpsArea> getAreaByCode(String code, Long baseId);
	List<HpsArea> getAreaByName(String name, Long baseId);
}
