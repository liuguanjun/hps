package org.appfuse.dao.hps.impl;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsQunuanfeiYouhuiDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
@Repository
public class HpsQunuanfeiYouhuiDaoHibernate  extends HpsGenericDaoHibernate<HpsHeatingShenfenXingzhiPreferential, Long> implements HpsQunuanfeiYouhuiDao {
	public HpsQunuanfeiYouhuiDaoHibernate() {
		super(HpsHeatingShenfenXingzhiPreferential.class);
	}

	@Override
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhui(
			QunuanfeiDiscountQueryParam param) {
		Query qry = createQuery("from HpsHeatingShenfenXingzhiPreferential t where t.paymentDate.id=:paymentDateId");
        qry.setLong("paymentDateId", param.getPaymentDateId());
        List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferentialList = qry.list();
        return heatingShenfenXingzhiPreferentialList;
	}

	@Override
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhui(
			QunuanfeiDiscountQueryParam param) {
		Query qry = createQuery("from HpsHeatingPaymentDatePreferential t where t.paymentDate.id=:paymentDateId");
        qry.setLong("paymentDateId", param.getPaymentDateId());
        List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferentialList = qry.list();
        return heatingPaymentDatePreferentialList;
	}

	@Override
	public HpsHeatingShenfenXingzhiPreferential addShenfenxingzhiYouhui(
			HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential) {
		saveOrUpdate(heatingShenfenXingzhiPreferential);
        flush();
        return heatingShenfenXingzhiPreferential;
	}

	@Override
	public HpsHeatingPaymentDatePreferential addPaymentYouhui(
			HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential) {
		saveOrUpdate(heatingPaymentDatePreferential);
        flush();
		return heatingPaymentDatePreferential;
	}

	@Override
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhuiById(
			Long shenfenxingzhiYouhuiId) {
		Query qry = createQuery("from HpsHeatingShenfenXingzhiPreferential t where t.id=:shenfenxingzhiYouhuiId");
        qry.setLong("shenfenxingzhiYouhuiId", shenfenxingzhiYouhuiId);
        List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferentialList = qry.list();
        return heatingShenfenXingzhiPreferentialList;
	}

	@Override
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiById(
			Long paymentYouhuiId) {
		Query qry = createQuery("from HpsHeatingPaymentDatePreferential t where t.id=:paymentYouhuiId");
        qry.setLong("paymentYouhuiId", paymentYouhuiId);
        List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferentialList = qry.list();
        return heatingPaymentDatePreferentialList;
	}
	
	@Override
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiRecord(Long paymentDateId, String shenfenxingzhiCode, Double payRate) {
		Query qry = createQuery("from HpsHeatingShenfenXingzhiPreferential t where t.paymentDate.id=:paymentDateId and t.shenfengXingzhi.code=:shenfenxingzhiCode and t.payRate=:payRate");
		qry.setLong("paymentDateId", paymentDateId);
		qry.setString("shenfenxingzhiCode", shenfenxingzhiCode);
		qry.setDouble("payRate", payRate);
        List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferentialList = qry.list();
        return heatingShenfenXingzhiPreferentialList;
	}

	@Override
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiRecord(Long paymentDateId, 
			Date startDate, Date endDate, Double payRate) {
		Query qry = createQuery("from HpsHeatingPaymentDatePreferential t where t.paymentDate.id=:paymentDateId and t.startDate=:startDate and t.endDate=:endDate and t.offRate=:payRate");
		qry.setLong("paymentDateId", paymentDateId);
		qry.setDate("startDate", startDate);
		qry.setDate("endDate", endDate);
		qry.setDouble("payRate", payRate);
        List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferentialList = qry.list();
        return heatingPaymentDatePreferentialList;
	}
	
	

}
