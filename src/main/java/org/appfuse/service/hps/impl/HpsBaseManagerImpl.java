package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsBaseDao;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.hps.HpsBaseManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.exception.AreaExistsException;
import com.my.hps.webapp.exception.UserAccountExistsException;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;

@Service
public class HpsBaseManagerImpl extends GenericManagerImpl<HpsBase, Long> implements HpsBaseManager {
	
	private HpsBaseDao baseDao;

	@Override
	@Transactional
	public HpsBase getBaseByCode(String code) {
		return baseDao.getBaseByCode(code);
	}
	
	@Override
	@Transactional
	public HpsBase getBaseById(Long id) {
		return baseDao.getBaseById(id);
	}
	
	@Override
	@Transactional
	public void deleteBaseTreeElement (Long areaId) {
		baseDao.deleteBaseTreeElement(areaId);
	}
	
	@Override
	@Transactional
	public List<HpsBase> getBases() {
		return baseDao.getAll();
	}
	
	@Override
	@Transactional
	public HpsArea saveHpsArea(HpsArea area) throws AreaExistsException {
		// 判断数据库中是否存在区域code
		List<HpsArea> hpsAreaList = baseDao.getAreaByCode(area.getCode(), area.getBase().getId());
		if (hpsAreaList.size() != 0) {
			throw new AreaExistsException("区域Code: '" + area.getCode() 
            		+ "' 已经存在!");
		}
		// 判断数据库中是否存在区域name
		hpsAreaList = baseDao.getAreaByName(area.getName(), area.getBase().getId());
		if (hpsAreaList.size() != 0) {
			throw new AreaExistsException("区域名称: '" + area.getName() 
            		+ "' 已经存在!");
		}
		// 添加区域
		baseDao.saveHpsArea(area);
		return area;
	}
	
	@Autowired
	public void setDao(HpsBaseDao dao) {
		this.dao = dao;
		this.baseDao = dao;
	}

}
