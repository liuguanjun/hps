package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsWeixiufeiUnitDao;
import org.appfuse.service.hps.HpsWeixiufeiUnitManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.WeixiufeiDiscountQueryParam;
import com.my.hps.webapp.exception.WeixiufeiUnitExistsException;
import com.my.hps.webapp.model.HpsMaintainUnit;
@Service
public class HpsWeixiufeiUnitManagerImpl extends GenericManagerImpl<HpsMaintainUnit, Long> implements HpsWeixiufeiUnitManager {

	private HpsWeixiufeiUnitDao weixiufeiUnitDao;
	
	@Override
	@Transactional
	public HpsMaintainUnit addWeixiufeiUnit(HpsMaintainUnit weixiufeiUnit) {
		
		// 判断数据库中是否存在
		List<HpsMaintainUnit> weixiufeiPaymentDateList = 
				weixiufeiUnitDao.getUnitRecord(
						weixiufeiUnit.getPaymentDate().getId(), 
						weixiufeiUnit.getYongfangXingzhi().getCode(),
						weixiufeiUnit.getUnit());
		if (weixiufeiPaymentDateList.size() != 0) {
			throw new WeixiufeiUnitExistsException("房屋性质：" + weixiufeiUnit.getYongfangXingzhi().getName() 
					+ "单价：" + weixiufeiUnit.getUnit() + "' 已经存在!");
		}
		
		return weixiufeiUnitDao.addWeixiufeiUnit(weixiufeiUnit);
	}
	
	@Override
	@Transactional
	public HpsMaintainUnit updateWeixiufeiUnit(HpsMaintainUnit weixiufeiUnit) {
		
		return weixiufeiUnitDao.addWeixiufeiUnit(weixiufeiUnit);
	}
	
	@Override
	@Transactional
	public List<HpsMaintainUnit> getWeixiufeiUnit(WeixiufeiDiscountQueryParam param) {
		return weixiufeiUnitDao.getWeixiufeiUnit(param);
	}
	
	@Override
	@Transactional
	public List<HpsMaintainUnit> getWeixiufeiUnitById(Long id) {
		return weixiufeiUnitDao.getWeixiufeiUnitById(id);
	}

	@Autowired
	public void setWeixiufeiPaymentDateDao(
			HpsWeixiufeiUnitDao weixiufeiUnitDao) {
		this.weixiufeiUnitDao = weixiufeiUnitDao;
	}
	
}
