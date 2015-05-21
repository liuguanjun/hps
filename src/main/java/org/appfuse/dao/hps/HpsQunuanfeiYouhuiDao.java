package org.appfuse.dao.hps;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;

public interface HpsQunuanfeiYouhuiDao extends GenericDao<HpsHeatingShenfenXingzhiPreferential, Long> {
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhui(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiRecord(Long paymentDateId, String shenfenxingzhiCode, Double payRate);
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhuiById(Long shenfenxingzhiYouhuiId);
	List<HpsHeatingPaymentDatePreferential> getPaymentYouhui(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiRecord(Long paymentDateId, Date startDate, Date endDate, Double payRate);
	List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiById(Long paymentYouhuiId);
	HpsHeatingShenfenXingzhiPreferential addShenfenxingzhiYouhui(HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential);
	HpsHeatingPaymentDatePreferential addPaymentYouhui(HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential);
}
