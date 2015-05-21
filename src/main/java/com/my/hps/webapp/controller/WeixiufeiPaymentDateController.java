package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsWeixiufeiPaymentDateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.model.HpsMaintainPaymentDate;

@Controller
@RequestMapping("/weixiufeiPaymentDate/")
public class WeixiufeiPaymentDateController extends BaseFormController {
	private HpsWeixiufeiPaymentDateManager weixiufeiPaymentDateManager;

	@Autowired
	public void setWeixiufeiPaymentDateManager(
			HpsWeixiufeiPaymentDateManager weixiufeiPaymentDateManager) {
		this.weixiufeiPaymentDateManager = weixiufeiPaymentDateManager;
	}
	
	/**
	 * 添加缴费标题，缴费日期，滞纳金。
	 * @return HpsMaintainPaymentDate
	 */
	@RequestMapping(method=RequestMethod.POST,value="addWeixiufeiPayment")
	@ResponseBody
	public HpsMaintainPaymentDate addPaymentDateAndZhinajin(@ModelAttribute HpsMaintainPaymentDate weixiufeiPaymentDate) {
		HpsMaintainPaymentDate hpsWeixiufeiPaymentDate;
		if (weixiufeiPaymentDate.getId() == null) {
			hpsWeixiufeiPaymentDate = weixiufeiPaymentDateManager.addPaymentDate(weixiufeiPaymentDate);
		} else {
			hpsWeixiufeiPaymentDate = weixiufeiPaymentDateManager.updatePaymentDate(weixiufeiPaymentDate);
		}
		
		return hpsWeixiufeiPaymentDate;
	}
	
	/**
	 * 取得缴费标题，缴费日期，滞纳金。
	 * @return List<HpsMaintainPaymentDate>
	 */
	@RequestMapping(method=RequestMethod.GET,value="getWeixiufeiPayment/{baseId}")
	@ResponseBody
	public List<HpsMaintainPaymentDate> getPaymentDateAndZhinajin(@PathVariable Long baseId) {
		List<HpsMaintainPaymentDate> weixiufeiPaymentDateList = weixiufeiPaymentDateManager.getPaymentDate(baseId);
		return weixiufeiPaymentDateList;
	}
	
	/**
	 * 点击编辑按钮，取得缴费标题，缴费日期，滞纳金。
	 * @return List<HpsMaintainPaymentDate>
	 */
	@RequestMapping(method=RequestMethod.GET,value="getPaymentDateById/{id}")
	@ResponseBody
	public List<HpsMaintainPaymentDate> getPaymentDateAndZhinajinById(@PathVariable Long id) {
		List<HpsMaintainPaymentDate> weixiufeiPaymentDateList = weixiufeiPaymentDateManager.getPaymentDateById(id);
		return weixiufeiPaymentDateList;
	}

}
