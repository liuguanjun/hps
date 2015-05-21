package org.appfuse.service.hps;

import java.util.List;

import com.my.hps.webapp.exception.AreaExistsException;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;

/**
 * 管理基地的服务接口
 * 
 * @author liuguanjun
 *
 */
public interface HpsBaseManager {
	
	HpsBase getBaseByCode(String code);
	HpsBase getBaseById(Long id);
	List<HpsBase> getBases();
	void deleteBaseTreeElement (Long areaId);
	HpsArea saveHpsArea(HpsArea area) throws AreaExistsException;
}
