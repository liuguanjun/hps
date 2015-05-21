package org.appfuse.dao.hps.impl;

import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsWeixiufeiUnitDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.WeixiufeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsMaintainUnit;
@Repository
public class HpsWeixiufeiUnitDaoHibernate extends HpsGenericDaoHibernate<HpsMaintainUnit, Long> implements HpsWeixiufeiUnitDao {
	
	public HpsWeixiufeiUnitDaoHibernate() {
		super(HpsMaintainUnit.class);
	}

	@Override
	public List<HpsMaintainUnit> getWeixiufeiUnit(
			WeixiufeiDiscountQueryParam param) {
		Query qry = createQuery("from HpsMaintainUnit t where t.paymentDate.id=:paymentDateId");
        qry.setLong("paymentDateId", param.getPaymentDateId());
        List<HpsMaintainUnit> weixiufeiUnitList = qry.list();
        return weixiufeiUnitList;
	}

	@Override
	public List<HpsMaintainUnit> getWeixiufeiUnitById(Long id) {
		Query qry = createQuery("from HpsMaintainUnit t where t.id=:id");
        qry.setLong("id", id);
        List<HpsMaintainUnit> weixiufeiUnitList = qry.list();
        return weixiufeiUnitList;
	}
	
	@Override
	public List<HpsMaintainUnit> getUnitRecord(Long paymentDateId,
			String yongfangxingzhiCode, Double unit) {
		Query qry = createQuery("from HpsMaintainUnit t where t.paymentDate.id=:paymentDateId and t.yongfangXingzhi.code=:yongfangxingzhiCode and t.unit=:unit");
		qry.setLong("paymentDateId", paymentDateId);
		qry.setString("yongfangxingzhiCode", yongfangxingzhiCode);
		qry.setDouble("unit", unit);
        List<HpsMaintainUnit> weixiufeiUnitList = qry.list();
        return weixiufeiUnitList;
	}

	@Override
	public HpsMaintainUnit addWeixiufeiUnit(HpsMaintainUnit weixiufeiUnit) {
		saveOrUpdate(weixiufeiUnit);
        flush();
        return weixiufeiUnit;
	}

	
	
}
