package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingUnit;

public interface HpsQunuanfeiUnitDao extends GenericDao<HpsHeatingUnit, Long> {
	
	List<HpsHeatingUnit> getQunuanfeiUnit(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingUnit> getQunuanfeiUnitById(Long id);
	List<HpsHeatingUnit> getUnitRecord(Long paymentDateId, String shenfenxingzhiCode, Double unit);
	HpsHeatingUnit addQunuanfeiUnit(HpsHeatingUnit heatingUnit);
	
	
}