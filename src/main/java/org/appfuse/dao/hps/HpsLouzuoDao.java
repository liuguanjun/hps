package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsLouzuo;

public interface HpsLouzuoDao extends GenericDao<HpsLouzuo, Long> {
	HpsLouzuo saveHpsLouzuo(HpsLouzuo louzuo);
	void deleteLouzuo(Long louzuoId);
	List<HpsLouzuo> getHpsLouzuoByArea(Long areaId);
	List<HpsLouzuo> getHpsLouzuoByBase(Long baseId);
	List<HpsLouzuo> getLouzuoByCode(String code, Long areaId);
	List<HpsLouzuo> getLouzuoByName(String name, Long areaId);
	List<HpsLouzuo> getLouzuoByAreaCode(String areaCode);
	Long getAreaIdByName(String name);
}