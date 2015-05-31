package org.appfuse.dao.hps.impl;

import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsHeatingMaintainCharge2015Dao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsHeatingMaintainChargeRecord2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;

@Repository
public class HpsHeatingMaintainCharge2015DaoHibernate extends HpsGenericDaoHibernate<HpsHeatingMaintainChargeRecord2015, Long>
        implements HpsHeatingMaintainCharge2015Dao {

    public HpsHeatingMaintainCharge2015DaoHibernate() {
        super(HpsHeatingMaintainChargeRecord2015.class);
    }

    @Override
    public List<HpsHeatingMaintainPaymentDate2015> getPaymentDates() {
        String hql = "from HpsHeatingMaintainPaymentDate2015 order by base.code";
        Query query = createQuery(hql);
        return query.list();
    }

    @Override
    public HpsHeatingMaintainPaymentDate2015 getPaymentDate(Long paymentDateId) {
        Criteria c = createCriteria(HpsHeatingMaintainPaymentDate2015.class);
        c.add(Restrictions.eq("id", paymentDateId));
        return (HpsHeatingMaintainPaymentDate2015) c.uniqueResult();
    }

    @Override
    public HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(HeatingMaintainCharge2015QueryParam param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HpsHeatingMaintainPaymentDate2015 savePaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate) {
        super.saveOrUpdate(paymentDate);
        return paymentDate;
    }

    @Override
    public HpsHeatingMaintainChargeRecord2015 getChargeRecordByHouseId(Long houseId) {
        Criteria c = createCriteria(HpsHeatingMaintainChargeRecord2015.class);
        c.add(Restrictions.eq("house.id", houseId));
        return (HpsHeatingMaintainChargeRecord2015) c.uniqueResult();
    }


}
