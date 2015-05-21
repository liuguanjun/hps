package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsQunuanfeiUnitDao;
import org.appfuse.service.hps.HpsQunuanfeiUnitManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.exception.QunuanfeiUnitExistsException;
import com.my.hps.webapp.model.HpsHeatingUnit;
@Service
public class HpsQunuanfeiUnitManagerImpl extends GenericManagerImpl<HpsHeatingUnit, Long> implements HpsQunuanfeiUnitManager {

	private HpsQunuanfeiUnitDao heatingUnitDao;
	
	@Override
	@Transactional
	public HpsHeatingUnit addQunuanfeiUnit(HpsHeatingUnit heatingUnit) {
		
		// 判断数据库中是否存在
		List<HpsHeatingUnit> heatingPaymentDateList = 
				heatingUnitDao.getUnitRecord(
						heatingUnit.getPaymentDate().getId(), 
						heatingUnit.getYongfangXingzhi().getCode(),
						heatingUnit.getUnit());
		if (heatingPaymentDateList.size() != 0) {
			throw new QunuanfeiUnitExistsException("房屋性质：" + heatingUnit.getYongfangXingzhi().getName() 
					+ "单价：" + heatingUnit.getUnit() + "' 已经存在!");
		}
		
		return heatingUnitDao.addQunuanfeiUnit(heatingUnit);
	}
	
	@Override
	@Transactional
	public HpsHeatingUnit updateQunuanfeiUnit(HpsHeatingUnit heatingUnit) {
		
		return heatingUnitDao.addQunuanfeiUnit(heatingUnit);
	}
	
	@Override
	@Transactional
	public List<HpsHeatingUnit> getQunuanfeiUnit(QunuanfeiDiscountQueryParam param) {
		return heatingUnitDao.getQunuanfeiUnit(param);
	}
	
	@Override
	@Transactional
	public List<HpsHeatingUnit> getQunuanfeiUnitById(Long id) {
		return heatingUnitDao.getQunuanfeiUnitById(id);
	}

	@Autowired
	public void setHeatingPaymentDateDao(
			HpsQunuanfeiUnitDao heatingUnitDao) {
		this.heatingUnitDao = heatingUnitDao;
	}
	
}
