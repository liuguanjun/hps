package com.my.hps.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.hps.HpsElectricChargeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.ElectricChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.PaginationQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChargeStateView;
import com.my.hps.webapp.controller.vo.HpsElectricUserTongjiRowView;
import com.my.hps.webapp.controller.vo.HpsElectricUserTongjiView;
import com.my.hps.webapp.model.ElectricChargeState;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.PaginationResult;

@Controller
@RequestMapping("/elecharge/")
public class ElectricChargeController extends BaseFormController {
	
	private HpsElectricChargeManager chargeManager;
	
	@RequestMapping(method = RequestMethod.GET, value = "chargestates")
	@ResponseBody
	public PaginationResult<ElectricChargeStateView> getChargeStates(@ModelAttribute ElectricChargeQueryParam param) {
		PaginationResult<ElectricChargeState> states = chargeManager.getChargeStates(param);
		List<ElectricChargeStateView> viewStates = ElectricChargeStateView.convertFromList(states.getRows());
		PaginationResult<ElectricChargeStateView> result = new PaginationResult<ElectricChargeStateView>();
		result.setRows(viewStates);
		result.setTotal(states.getTotal());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "usertongjiresult")
	@ResponseBody
	public HpsElectricUserTongjiView getUserTongjiResult(@ModelAttribute ElectricTongjiQueryParam param) {
		List<HpsElectricUserTongjiRowView> rows = chargeManager.getUserTongjiRowList(param);
		HpsElectricUserTongjiView result = new HpsElectricUserTongjiView();
		result.setRows(rows);
		List<HpsElectricUserTongjiRowView> footer = new ArrayList<HpsElectricUserTongjiRowView>();
		HpsElectricUserTongjiRowView footerRow = new HpsElectricUserTongjiRowView();
		footerRow.setOperUser(null);
		footerRow.setOperName("合计：");
		double electricCharge = 0;
		double zhinajin = 0;
		double weishengfei = 0;
		double paiwufei = 0;
		double zhaomingfei = 0;
		Long juminMonthCountSum = 0l;
		double actualCharge = 0;
		int receiptCnt = 0;
		long electricCount = 0l;
		for (HpsElectricUserTongjiRowView row : rows) {
			Double rowActualCharge = row.getActualCharge();
			if (rowActualCharge != null) {
				actualCharge += rowActualCharge;
			}
			Double rowElectricCharge = row.getElectricCharge();
			Double rowZhinajin = row.getZhinajin();
			Double rowWeishengfei = row.getWeishengfei();
			Double rowPaiwufei = row.getPaiwufei();
			Double rowZhaomingfei = row.getZhaomingfei();
			long rowElectricCount = row.getElectricCount();
			if (rowElectricCharge != null) {
				electricCharge += rowElectricCharge;
			}
			if (rowZhinajin != null) {
				zhinajin += rowZhinajin;
			}
			if (rowWeishengfei != null) {
				weishengfei += rowWeishengfei;
			}
			if (rowPaiwufei != null) {
				paiwufei += rowPaiwufei;
			}
			if (rowZhaomingfei != null) {
				zhaomingfei += rowZhaomingfei;
			}
			Long juminMonthCount = row.getJuminMonthCount();
			juminMonthCountSum += juminMonthCount;
			receiptCnt += row.getReceiptCnt();
			electricCount += rowElectricCount;
		}
		footerRow.setElectricCharge(electricCharge);
		footerRow.setReceiptCnt(receiptCnt);
		footerRow.setPaiwufei(paiwufei);
		footerRow.setWeishengfei(weishengfei);
		footerRow.setZhaomingfei(zhaomingfei);
		footerRow.setZhinajin(zhinajin);
		footerRow.setActualCharge(actualCharge);
		footerRow.setJuminMonthCount(juminMonthCountSum);
		footerRow.setElectricCount(electricCount);
		footer.add(footerRow);
		result.setFooter(footer);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{houseId}/chargestate")
	@ResponseBody
	public ElectricChargeStateView getChargeState(@PathVariable Long houseId) {
		// 页面获取的缴费状态，都是不计算滞纳金的，只有在缴费的时候，由收费员选择是否缴纳滞纳金
		ElectricChargeState state = chargeManager.getChargeState(houseId, false);
		ElectricChargeStateView viewState = ElectricChargeStateView.convertFromBean(state);
		return viewState;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "chargerecord")
	@ResponseBody
	public HpsElectricChargeRecord charge(@RequestParam Long houseId, @RequestParam Double chargeValue, 
			@RequestParam(required = false, defaultValue = "false") boolean zhinajinOn) {
		HpsElectricChargeRecord chargeRecord = chargeManager.charge(houseId, chargeValue, zhinajinOn);
		return chargeRecord;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{recordId}/cancelledcause")
	@ResponseBody
	public HpsElectricChargeRecord cancelChargeRecord(@PathVariable Long recordId, 
				@RequestParam String cause) {
		HpsElectricChargeRecord record = chargeManager.cancelChargeRecord(recordId, cause);
		return record;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{houseId}/initsurplus")
	@ResponseBody
	public HpsElectricChargeRecord initHouseSurplus(@PathVariable Long houseId, 
				@RequestParam(value = "initSurplus", required = true) Double initSurplus,
				@RequestParam(value = "initElectricReadout", required = true) Integer initElectricReadout) {
		HpsElectricChargeRecord record = chargeManager.initHouseSurplus(houseId, initSurplus, initElectricReadout);
		return record;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{recordId}/chargerecord")
	@ResponseBody
	public HpsElectricChargeRecord getChargeRecord(@PathVariable Long recordId) {
		HpsElectricChargeRecord record = chargeManager.get(recordId);
		return record;
	}
	
	/**
	 * 根据缴费Id获取与其对应的ElectricChargeStateView对象
	 * 此方法用户查看缴费记录历史的详细内容
	 * 
	 * @param recordId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{recordId}/chargerecordstate")
	@ResponseBody
	public ElectricChargeStateView getChargeRecordState(@PathVariable Long recordId) {
		HpsElectricChargeRecord record = chargeManager.get(recordId);
		ElectricChargeStateView viewState = ElectricChargeStateView.convertFromBean(record);
		return viewState;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{houseId}/chargerecords")
	@ResponseBody
	public PaginationResult<HpsElectricChargeRecord> getChargeRecords(@PathVariable Long houseId, @ModelAttribute PaginationQueryParam param) {
		PaginationResult<HpsElectricChargeRecord> records = chargeManager.getChargeRecords(houseId, param);
		return records;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "operchargerecords")
	@ResponseBody
	public PaginationResult<HpsElectricChargeRecord> getOperChargeRecords(@ModelAttribute ElectricTongjiQueryParam param) {
		PaginationResult<HpsElectricChargeRecord> records = chargeManager.getChargeRecords(param);
		return records;
	}

	@Autowired
	public void setChargeManager(HpsElectricChargeManager chargeManager) {
		this.chargeManager = chargeManager;
	}
	
	

}
