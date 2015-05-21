package org.appfuse.dao.hps.impl;

import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsQunuanfeiUnitDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;
@Repository
public class HpsQunuanfeiUnitDaoHibernate extends HpsGenericDaoHibernate<HpsHeatingUnit, Long> implements HpsQunuanfeiUnitDao {
	
	public HpsQunuanfeiUnitDaoHibernate() {
		super(HpsHeatingUnit.class);
	}

	@Override
	public List<HpsHeatingUnit> getQunuanfeiUnit(
			QunuanfeiDiscountQueryParam param) {
		Query qry = createQuery("from HpsHeatingUnit t where t.paymentDate.id=:paymentDateId");
        qry.setLong("paymentDateId", param.getPaymentDateId());
        List<HpsHeatingUnit> heatingUnitList = qry.list();
        return heatingUnitList;
	}

	@Override
	public List<HpsHeatingUnit> getQunuanfeiUnitById(Long id) {
		Query qry = createQuery("from HpsHeatingUnit t where t.id=:id");
        qry.setLong("id", id);
        List<HpsHeatingUnit> heatingUnitList = qry.list();
        return heatingUnitList;
	}
	
	@Override
	public List<HpsHeatingUnit> getUnitRecord(Long paymentDateId,
			String yongfangxingzhiCode, Double unit) {
		Query qry = createQuery("from HpsHeatingUnit t where t.paymentDate.id=:paymentDateId and t.yongfangXingzhi.code=:yongfangxingzhiCode and t.unit=:unit");
		qry.setLong("paymentDateId", paymentDateId);
		qry.setString("yongfangxingzhiCode", yongfangxingzhiCode);
		qry.setDouble("unit", unit);
        List<HpsHeatingUnit> heatingUnitList = qry.list();
        return heatingUnitList;
	}

	@Override
	public HpsHeatingUnit addQunuanfeiUnit(HpsHeatingUnit heatingUnit) {
		saveOrUpdate(heatingUnit);
        flush();
        return heatingUnit;
	}

	
	
}
