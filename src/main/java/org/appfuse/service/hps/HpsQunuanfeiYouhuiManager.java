package org.appfuse.service.hps;

import java.util.Date;
import java.util.List;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;

public interface HpsQunuanfeiYouhuiManager {
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhui(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhuiById(Long shenfenxingzhiYouhuiId);
	List<HpsHeatingPaymentDatePreferential> getPaymentYouhui(QunuanfeiDiscountQueryParam param);
	List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiById(Long paymentYouhuiId);
	HpsHeatingShenfenXingzhiPreferential addShenfenxingzhiYouhui(HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential);
	HpsHeatingPaymentDatePreferential addPaymentYouhui(HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential);
	HpsHeatingShenfenXingzhiPreferential updateShenfenxingzhiYouhui(HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential);
	HpsHeatingPaymentDatePreferential updatePaymentYouhui(HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential);
	

}
