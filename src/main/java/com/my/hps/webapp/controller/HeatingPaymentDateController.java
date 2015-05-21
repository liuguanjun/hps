package com.my.hps.webapp.controller;

import java.util.Date;
import java.util.List;

import org.appfuse.service.hps.HpsHeatingPaymentDateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.model.HpsHeatingPaymentDate;

@Controller
@RequestMapping("/heatingPaymentDate/")
public class HeatingPaymentDateController extends BaseFormController {
	private HpsHeatingPaymentDateManager heatingPaymentDateManager;

	@Autowired
	public void setHeatingPaymentDateManager(
			HpsHeatingPaymentDateManager heatingPaymentDateManager) {
		this.heatingPaymentDateManager = heatingPaymentDateManager;
	}
	
	/**
	 * 添加缴费标题，缴费日期，滞纳金。
	 * @return HpsHeatingPaymentDate
	 */
	@RequestMapping(method=RequestMethod.POST,value="addHeatPaymentAndZhinajin")
	@ResponseBody
	public HpsHeatingPaymentDate addPaymentDateAndZhinajin(@ModelAttribute HpsHeatingPaymentDate heatingPaymentDate) {
		heatingPaymentDate.setZhinajinRate(heatingPaymentDate.getZhinajinRate()/1000);
		heatingPaymentDate.setStopHeatingRate(heatingPaymentDate.getStopHeatingRate()/100);
		heatingPaymentDate.setLivingSoHardRate(heatingPaymentDate.getLivingSoHardRate()/100);
		HpsHeatingPaymentDate hpsHeatingPaymentDate;
		if (heatingPaymentDate.getId() == null) {
			hpsHeatingPaymentDate = heatingPaymentDateManager.addPaymentDateAndZhinajin(heatingPaymentDate);
		} else {
			hpsHeatingPaymentDate = heatingPaymentDateManager.updatePaymentDateAndZhinajin(heatingPaymentDate);
		}
		
		return hpsHeatingPaymentDate;
	}
	
	/**
	 * 取得缴费标题，缴费日期，滞纳金。
	 * @return List<HpsHeatingPaymentDate>
	 */
	@RequestMapping(method=RequestMethod.GET,value="getHeatPaymentAndZhinajin/{baseId}")
	@ResponseBody
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajin(@PathVariable Long baseId) {
		List<HpsHeatingPaymentDate> heatingPaymentDateList = heatingPaymentDateManager.getPaymentDateAndZhinajin(baseId);
		return heatingPaymentDateList;
	}
	
	/**
	 * 点击编辑按钮，取得缴费标题，缴费日期，滞纳金。
	 * @return List<HpsHeatingPaymentDate>
	 */
	@RequestMapping(method=RequestMethod.GET,value="getPaymentDateAndZhinajinById/{id}")
	@ResponseBody
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajinById(@PathVariable Long id) {
		List<HpsHeatingPaymentDate> heatingPaymentDateList = heatingPaymentDateManager.getPaymentDateAndZhinajinById(id);
		return heatingPaymentDateList;
	}

}
