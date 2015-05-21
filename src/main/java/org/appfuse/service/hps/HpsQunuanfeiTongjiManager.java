package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.controller.queryparam.QunuanfeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsQunuanfeiTongjiView;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsUser;

public interface HpsQunuanfeiTongjiManager extends GenericManager<HpsHeatingChargeRecord, Long> {
	List<HpsUser> getCaozuoyuan(String baseCode);
	// 取得收费户数
	void getQunuanfeiTongjiHushu(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	// 计划总额,实缴合计
	void getQunuanfeiTongjiJihuaAndHushu(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得全额收费(例),全额收费额
	void getQunuanfeiTongjiQuanE(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得惠后收费(例),惠后收费额
	void getQunuanfeiTongjiHuihou(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得停供收费(例),停供收费额
	void getQunuanfeiTongjiTinggong(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得困难住户收费(例),困难住户收费额
	void getQunuanfeiTongjiKunnan(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得滞纳缴费(例),滞纳缴费额
	void getQunuanfeiTongjiZhinajin(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得优惠减免额
	void getQunuanfeiTongjiYouhuiJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得停供减免额
	void getQunuanfeiTongjiTinggongJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
	// 取得困难住户减免额
	void getQunuanfeiTongjiKunnanJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView, QunuanfeiTongjiQueryParam param);
	
}
