package org.appfuse.service.hps.impl;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.search.expression.Criteria;

import org.appfuse.dao.hps.HpsLouzuoDao;
import org.appfuse.service.hps.HpsLouzuoManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.exception.LouzuoExistsException;
import com.my.hps.webapp.model.HpsLouzuo;
@Service
public class HpsLouzuoManagerImpl extends GenericManagerImpl<HpsLouzuo, Long> implements HpsLouzuoManager {

	private HpsLouzuoDao louzuoDao;
	
	@Override
	@Transactional
	public HpsLouzuo saveHpsLouzuo(HpsLouzuo louzuo)
			throws LouzuoExistsException {
		// 判断数据库中是否存在楼座code
		List<HpsLouzuo> hpsLouzuoList = louzuoDao.getLouzuoByCode(louzuo.getCode(), louzuo.getArea().getId());
		if (hpsLouzuoList.size() != 0) {
			throw new LouzuoExistsException("楼座Code: '" + louzuo.getCode() 
            		+ "' 已经存在!");
		}
		// 判断数据库中是否存在楼座name
		hpsLouzuoList = louzuoDao.getLouzuoByName(louzuo.getName(), louzuo.getArea().getId());
		if (hpsLouzuoList.size() != 0) {
			throw new LouzuoExistsException("楼座名称: '" + louzuo.getName() 
            		+ "' 已经存在!");
		}
		// 取得默认区域的ID
		long areaId = 0; 
		if ("口前基地".equals((louzuo.getArea().getName()))) {
			areaId = louzuoDao.getAreaIdByName("口前区域");
			louzuo.getArea().setId(areaId);
		} else if ("红石基地".equals((louzuo.getArea().getName()))) {
			areaId = louzuoDao.getAreaIdByName("红石区域");
			louzuo.getArea().setId(areaId);
		} else if ("白山基地".equals((louzuo.getArea().getName()))) {
			areaId = louzuoDao.getAreaIdByName("白山区域");
			louzuo.getArea().setId(areaId);
		} else if ("旅顺基地".equals((louzuo.getArea().getName()))) {
			areaId = louzuoDao.getAreaIdByName("旅顺区域");
			louzuo.getArea().setId(areaId);
		} 
		// 添加楼座
		louzuoDao.saveHpsLouzuo(louzuo);
		return louzuo;
	}

	@Override
	@Transactional
	public void deleteLouzuo(Long louzuoId) {
		louzuoDao.deleteLouzuo(louzuoId);
		
	}

	@Autowired
	public void setLouzuoDao(HpsLouzuoDao louzuoDao) {
		this.dao = louzuoDao;
		this.louzuoDao = louzuoDao;
	}
	
	@Override
	@Transactional
	public List<HpsLouzuo> getHpsLouzuoByArea(Long areaId) {        
        List<HpsLouzuo> hpsLouzuoList = louzuoDao.getHpsLouzuoByArea(areaId);
        Collections.sort(hpsLouzuoList);
        return hpsLouzuoList;
	}
	
	@Override
	@Transactional
	public List<HpsLouzuo> getHpsLouzuoByBase(Long baseId) {
		List<HpsLouzuo> hpsLouzuoList = louzuoDao.getHpsLouzuoByBase(baseId);
		Collections.sort(hpsLouzuoList);
        return hpsLouzuoList;
	}
	
	@Override
	@Transactional
	public List<HpsLouzuo> getHpsLouzuoByCode(String areaCode) {
		List<HpsLouzuo> hpsLouzuoList = louzuoDao.getLouzuoByAreaCode(areaCode);
		Collections.sort(hpsLouzuoList);
		return hpsLouzuoList;
	}

}
