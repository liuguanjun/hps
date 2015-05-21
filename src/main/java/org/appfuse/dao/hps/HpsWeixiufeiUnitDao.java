package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.WeixiufeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsMaintainUnit;

public interface HpsWeixiufeiUnitDao extends GenericDao<HpsMaintainUnit, Long> {
	
	List<HpsMaintainUnit> getWeixiufeiUnit(WeixiufeiDiscountQueryParam param);
	List<HpsMaintainUnit> getWeixiufeiUnitById(Long id);
	List<HpsMaintainUnit> getUnitRecord(Long paymentDateId, String shenfenxingzhiCode, Double unit);
	HpsMaintainUnit addWeixiufeiUnit(HpsMaintainUnit weixiufeiUnit);
	
	
}