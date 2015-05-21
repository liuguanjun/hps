package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.exception.LouzuoExistsException;
import com.my.hps.webapp.model.HpsLouzuo;

public interface HpsLouzuoManager extends GenericManager<HpsLouzuo, Long> {
	HpsLouzuo saveHpsLouzuo(HpsLouzuo louzuo) throws LouzuoExistsException;
	void deleteLouzuo(Long louzuoId);
	List<HpsLouzuo> getHpsLouzuoByArea(Long areaId);
	List<HpsLouzuo> getHpsLouzuoByCode(String areaCode);
	List<HpsLouzuo> getHpsLouzuoByBase(Long baseId);
}