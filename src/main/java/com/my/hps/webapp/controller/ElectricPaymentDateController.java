package com.my.hps.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.service.hps.HpsElectricPaymentDateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.vo.YearComboboxView;
import com.my.hps.webapp.model.HpsElectricPaymentDate;

@Controller
@RequestMapping("/admin/electricPaymentDate/")
public class ElectricPaymentDateController extends BaseFormController {
	
	private HpsElectricPaymentDateManager paymentDateManager;


	@RequestMapping(method=RequestMethod.GET,value="{baseCode}/paymentdates")
	@ResponseBody
	public List<HpsElectricPaymentDate> getPaymentDates(@PathVariable String baseCode,
			@RequestParam(required = false, value= "year") String year) {
		List<HpsElectricPaymentDate> paymentDates = paymentDateManager.getPaymentDates(baseCode);
		if (StringUtils.isNotEmpty(year)) {
			List<HpsElectricPaymentDate> result = new ArrayList<HpsElectricPaymentDate>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			for (HpsElectricPaymentDate paymentDate : paymentDates) {
				Date month = paymentDate.getMonth();
				if (format.format(month).equals(year)) {
					result.add(paymentDate);
				}
			}
			return result;
		} else {
			return paymentDates;
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,value="{baseCode}/paymentdatesbynow")
	@ResponseBody
	public List<HpsElectricPaymentDate> getPaymentDatesByNow(@PathVariable String baseCode) {
		List<HpsElectricPaymentDate> paymentDates = paymentDateManager.getPaymentDates(baseCode);
		List<HpsElectricPaymentDate> result = new ArrayList<HpsElectricPaymentDate>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String currentMonthStr = format.format(new Date());
		int currentMonthInt = Integer.parseInt(currentMonthStr);
		for (HpsElectricPaymentDate paymentDate : paymentDates) {
			Date month = paymentDate.getMonth();
			String paymentMonthStr = format.format(month);
			int paymentMonthInt = Integer.parseInt(paymentMonthStr);
			if (currentMonthInt > paymentMonthInt) {
				result.add(paymentDate);
			}
		}
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="{baseCode}/paymentyears")
	@ResponseBody
	public List<YearComboboxView> getPaymentYearsByNow(@PathVariable String baseCode) {
		List<HpsElectricPaymentDate> paymentDates = paymentDateManager.getPaymentDatesByNow(baseCode);
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		List<YearComboboxView> result = new ArrayList<YearComboboxView>();
		for (HpsElectricPaymentDate paymentDate : paymentDates) {
			Date month = paymentDate.getMonth();
			YearComboboxView yearView = new YearComboboxView(format.format(month));
			if (!result.contains(yearView)) {
				result.add(yearView);
			}
		}
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="paymentdate/{id}")
	@ResponseBody
	public HpsElectricPaymentDate getPaymentDate(@PathVariable Long id) {
		HpsElectricPaymentDate paymentDate = paymentDateManager.get(id);
		return paymentDate;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="paymentdate")
	@ResponseBody
	public HpsElectricPaymentDate updatePaymentDate(@ModelAttribute HpsElectricPaymentDate paymentDate) {
		HpsElectricPaymentDate paymentDateInDB = paymentDateManager.get(paymentDate.getId());
		paymentDateInDB.setStartDate(paymentDate.getStartDate());
		paymentDateInDB.setEndDate(paymentDate.getEndDate());
		paymentDateManager.save(paymentDateInDB);
		return paymentDateInDB;
	}

	@Autowired
	public void setPaymentDateManager(
			HpsElectricPaymentDateManager paymentDateManager) {
		this.paymentDateManager = paymentDateManager;
	}
	
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(java.sql.Date.class, 
//				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false)); 
//	}
	

}
