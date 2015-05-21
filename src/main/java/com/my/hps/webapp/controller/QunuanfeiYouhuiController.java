package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsQunuanfeiYouhuiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsLouzuo;

@Controller
@RequestMapping("/qunuanfeiYouhui/")
public class QunuanfeiYouhuiController extends BaseFormController {
	private HpsQunuanfeiYouhuiManager qunuanfeiYouhuiManager;
	private HpsDictManager dictManager;

	@Autowired
	public void setQunuanfeiYouhuiManager(
			HpsQunuanfeiYouhuiManager qunuanfeiYouhuiManager) {
		this.qunuanfeiYouhuiManager = qunuanfeiYouhuiManager;
	}
	
	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	/**
	 * 	取得身份性质优惠
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getShenfenxingzhiYouhui")
	@ResponseBody
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhui(@ModelAttribute QunuanfeiDiscountQueryParam param) {
		List<HpsHeatingShenfenXingzhiPreferential> shenfenxingzhiYouhui = qunuanfeiYouhuiManager.getShenfenxingzhiYouhui(param);
		return shenfenxingzhiYouhui;
	}
	
	/**
	 * 	取得编辑画面的身份性质优惠
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getShenfenxingzhiYouhuiById/{shenfenxingzhiYouhuiId}")
	@ResponseBody
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhuiById(@PathVariable Long shenfenxingzhiYouhuiId) {
		List<HpsHeatingShenfenXingzhiPreferential> shenfenxingzhiYouhui = qunuanfeiYouhuiManager.getShenfenxingzhiYouhuiById(shenfenxingzhiYouhuiId);
		return shenfenxingzhiYouhui;
	}
	
	/**
	 * 	取得优惠日期优惠
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getPaymentYouhui")
	@ResponseBody
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhui(@ModelAttribute QunuanfeiDiscountQueryParam param) {
		List<HpsHeatingPaymentDatePreferential> paymentDateYouhui = qunuanfeiYouhuiManager.getPaymentYouhui(param);
		return paymentDateYouhui;
	}
	
	/**
	 * 	取得编辑画面的优惠日期优惠
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getPaymentYouhuiById/{paymentYouhuiId}")
	@ResponseBody
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiById(@PathVariable Long paymentYouhuiId) {
		List<HpsHeatingPaymentDatePreferential> paymentDateYouhui = qunuanfeiYouhuiManager.getPaymentYouhuiById(paymentYouhuiId);
		return paymentDateYouhui;
	}
	
	/**
	 * 	添加身份性质优惠
	 * @return HpsHeatingShenfenXingzhiPreferential
	 */
	@RequestMapping(method=RequestMethod.POST,value="addShenfenxingzhiYouhui")
	@ResponseBody
	public HpsHeatingShenfenXingzhiPreferential addShenfenxingzhiYouhui(@ModelAttribute HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential) {
		heatingShenfenXingzhiPreferential.setPayRate(heatingShenfenXingzhiPreferential.getPayRate()/100);
		setCodeItems(heatingShenfenXingzhiPreferential);
		HpsHeatingShenfenXingzhiPreferential shenfenxingzhiYouhui;
		if (heatingShenfenXingzhiPreferential.getId() == null) {
			shenfenxingzhiYouhui = qunuanfeiYouhuiManager.addShenfenxingzhiYouhui(heatingShenfenXingzhiPreferential);
		} else {
			shenfenxingzhiYouhui = qunuanfeiYouhuiManager.updateShenfenxingzhiYouhui(heatingShenfenXingzhiPreferential);
		}
		return shenfenxingzhiYouhui;
	}
	
	/**
	 * 	添加优惠日期优惠
	 * @return HpsHeatingPaymentDatePreferential
	 */
	@RequestMapping(method=RequestMethod.POST,value="addPaymentYouhui")
	@ResponseBody
	public HpsHeatingPaymentDatePreferential addPaymentYouhui(@ModelAttribute HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential) {
		heatingPaymentDatePreferential.setOffRate(heatingPaymentDatePreferential.getOffRate()/100);
		HpsHeatingPaymentDatePreferential paymentDateYouhui;
		if (heatingPaymentDatePreferential.getId() == null) {
			paymentDateYouhui = qunuanfeiYouhuiManager.addPaymentYouhui(heatingPaymentDatePreferential);
		} else {
			paymentDateYouhui = qunuanfeiYouhuiManager.updatePaymentYouhui(heatingPaymentDatePreferential);
		}
		return paymentDateYouhui;
	}
	
	/**
	 * 通过code身份性质并重新设置到heatingShenfenXingzhiPreferential中，
	 */
	private void setCodeItems(HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential) {
		// 身份性质
		String shenfenXingzhiCode =heatingShenfenXingzhiPreferential.getShenfengXingzhi().getCode();
		HpsDictItem shenfenXingzhiItem = dictManager.getShenfenXingzhiDictItem(shenfenXingzhiCode);
		heatingShenfenXingzhiPreferential.setShenfengXingzhi(shenfenXingzhiItem);
	}

}
