package org.appfuse.service.hps.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hps.HpsHouseDao;
import org.appfuse.service.hps.HpsHeatingChargeManager;
import org.appfuse.service.hps.HpsHeatingMaintain2015ChargeManager;
import org.appfuse.service.hps.HpsHouseManager;
import org.appfuse.service.hps.HpsMaintainChargeManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.PaginationResult;

@Service
public class HpsHouseManagerImpl extends GenericManagerImpl<HpsHouse, Long>
		implements HpsHouseManager, ApplicationContextAware {

	private HpsHouseDao houseDao;
	private ApplicationContext appContext;

	@Override
	@Transactional
	public PaginationResult<HpsHouse> getHouses(HouseQueryParam queryParam) {
		return houseDao.getHouses(queryParam);
	}

	@Override
	@Transactional
	public HpsHouse getHouse(Long houseId) {
		return houseDao.getHouse(houseId);
	}

	@Override
	@Transactional
	public HpsHouse saveHouse(HpsHouse house) {
		HpsHouseOwner owner = house.getOwner();
		HpsHouse sameHouse = houseDao.getDuplicateHouse(house);
		if (sameHouse != null) {
			throw new RuntimeException(house.getLouzuo().getArea().getName().replace("区域", "基地") + 
					"房屋["+ house.getShortAddress() +"]已经登记过");
		}
		HpsHouse savedHouse = houseDao.saveHouse(house);
		// 房主号
		owner.setNo(StringUtils.leftPad(owner.getId().toString(), 5, '0'));
		// 房号
		savedHouse.setNo(StringUtils.leftPad(savedHouse.getId().toString(), 5,
				'0'));
		// 重新生成取暖费缴费记录
		HpsHeatingChargeManager heatingManager = appContext.getBean(HpsHeatingChargeManager.class);
		heatingManager.initializeChargeRecordCurrentYear(savedHouse);
		// 重新生成房屋维修费缴费记录
		HpsMaintainChargeManager maintainManager = appContext.getBean(HpsMaintainChargeManager.class);
		maintainManager.initializeChargeRecordCurrentYear(savedHouse);
		// 重新生成取暖改造缴费记录
		HpsHeatingMaintain2015ChargeManager heatingMaintainManager = appContext.getBean(HpsHeatingMaintain2015ChargeManager.class);
		heatingMaintainManager.initializeChargeRecord(savedHouse);
		return savedHouse;
	}
	
	@Override
	@Transactional
	public List<HpsHouseOwner> getHouseOwners(String name, String idCardNo) {
		return houseDao.getHouseOwners(name, idCardNo);
	}

	@Override
	@Transactional
	public void removeHouse(Long... houseIds) {
		houseDao.removeHouse(houseIds);
	}

	@Autowired
	public void setHouseDao(HpsHouseDao houseDao) {
		this.houseDao = houseDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
	}

}
