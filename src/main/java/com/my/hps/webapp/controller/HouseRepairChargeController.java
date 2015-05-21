package com.my.hps.webapp.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.appfuse.service.hps.HpsMaintainChargeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.MaintainChargeQueryParam;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 房屋维修费页面Controller
 * 
 * @author liuguanjun
 *
 */
@Controller
@RequestMapping("/maintaincharge/")
public class HouseRepairChargeController {
	
	private HpsMaintainChargeManager chargeManger;

	@RequestMapping(method = RequestMethod.GET, value = "{baseCode}/paymentdates")
	@ResponseBody
	public List<HpsMaintainPaymentDate> getPaymentDates(@PathVariable String baseCode) {
		List<HpsMaintainPaymentDate> paymentDates = chargeManger.getPaymentDates(baseCode);
		return paymentDates;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "chargerecords")
	@ResponseBody
	public PaginationResult<HpsMaintainChargeRecord> getChargeRecords(@ModelAttribute MaintainChargeQueryParam param) {
		PaginationResult<HpsMaintainChargeRecord> result = chargeManger.getChargeRecords(param);
		List<HpsMaintainChargeRecord> records = result.getRows();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		for (HpsMaintainChargeRecord record : records) {
			List<HpsMaintainChargeRecord> divertedRecords = null;
			Double divertedCharge = 0d;
			StringBuilder sb = new StringBuilder();
			if (record.getChargeState() == ChargeStateEnum.CHARGED) {
				// 已缴费的状态，那么获取之前结转的缴费记录
				divertedRecords = chargeManger.getDivertedChargeRecords(record.getId());
			} else if (record.getChargeState() == ChargeStateEnum.UNCHARGED) {
				// 未缴费的状态，获取所有之前没交费的记录
				divertedRecords = chargeManger.getMustDivertChargeRecords(record);
//				// 未缴费状态，设置结转金额的初始值（对于系统初始数据导入时，记录之前的结转金额）
//				divertedCharge = record.getDivertedCharge();
//				if (divertedCharge == null) {
//					divertedCharge = 0d;
//				} else if (divertedCharge > 0) {
//					sb.append("初始历年结转：" + format.format(divertedCharge) + "元");
//				}
			} else {
				continue;
			}
			if (CollectionUtils.isEmpty(divertedRecords)) {
				continue;
			}

			for (HpsMaintainChargeRecord divertedRecord : divertedRecords) {
				double mustCharge = divertedRecord.getMustCharge();
				sb.append(divertedRecord.getPaymentDate().getTitle());
				sb.append(":");
				sb.append(format.format(mustCharge));
				sb.append("元    ");
				divertedCharge += mustCharge;
			}
			// 结转金额
			BigDecimal divertedChargeBigDecimal = new BigDecimal(divertedCharge);
			divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(2, RoundingMode.HALF_UP);
//			divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(1, RoundingMode.HALF_UP);
			record.setDivertedCharge(divertedChargeBigDecimal.doubleValue());
			// 结转描述
			record.setDivertedMsg(sb.toString());
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "chargerecord/{recordId}")
	@ResponseBody
	public HpsMaintainChargeRecord getChargeRecord(@PathVariable Long recordId) {
		HpsMaintainChargeRecord record = chargeManger.getChargeRecord(recordId);
		List<HpsMaintainChargeRecord> divertedRecords = null;
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Double divertedCharge = 0d;
		StringBuilder sb = new StringBuilder();
		if (record.getChargeState() == ChargeStateEnum.CHARGED) {
			// 已缴费的状态，那么获取之前结转的缴费记录
			divertedRecords = chargeManger.getDivertedChargeRecords(record.getId());
		} else if (record.getChargeState() == ChargeStateEnum.UNCHARGED) {
			// 未缴费的状态，获取所有之前没交费的记录
			divertedRecords = chargeManger.getMustDivertChargeRecords(record);
//			// 未缴费状态，设置结转金额的初始值（对于系统初始数据导入时，记录之前的结转金额）
//			divertedCharge = record.getDivertedCharge();
//			if (divertedCharge == null) {
//				divertedCharge = 0d;
//			} else if (divertedCharge > 0) {
//				sb.append("初始历年结转：" + format.format(divertedCharge) + "元");
//			}
		} else {
			return record;
		}
		for (HpsMaintainChargeRecord divertedRecord : divertedRecords) {
			double mustMaintainCharge = divertedRecord.getMustCharge();
			sb.append(divertedRecord.getPaymentDate().getTitle());
			sb.append(":");
			sb.append(format.format(mustMaintainCharge));
			sb.append("元    ");
			divertedCharge += mustMaintainCharge;
		}
		// 结转金额
		BigDecimal divertedChargeBigDecimal = new BigDecimal(divertedCharge);
		divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(2, RoundingMode.HALF_UP);
		//divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(1, RoundingMode.HALF_UP);
		record.setDivertedCharge(divertedChargeBigDecimal.doubleValue());
		// 结转描述
		record.setDivertedMsg(sb.toString());
		return record;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{recordId}/actualCharge")
	@ResponseBody
	public HpsMaintainChargeRecord charge(@PathVariable Long recordId, 
				@RequestParam Double actualCharge,
				@RequestParam(required = false, defaultValue = "12") int monthCount,
				@RequestParam(required = false, defaultValue = "false") boolean gratis,
				@RequestParam(required = false, defaultValue = "") String wageNum,
				@RequestParam(required = false, defaultValue = "") String remarks) {
		HpsMaintainChargeRecord record = chargeManger.charge(recordId, actualCharge, monthCount,
				gratis, wageNum, remarks);
		return record;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{recordId}/cancel")
	@ResponseBody
	public HpsMaintainChargeRecord cancel(@PathVariable Long recordId, 
				@RequestParam(required = false, defaultValue = "") String remarks) {
		HpsMaintainChargeRecord record = chargeManger.cancel(recordId, remarks);
		return record;
	}

	@Autowired
	public void setChargeManger(HpsMaintainChargeManager chargeManger) {
		this.chargeManger = chargeManger;
	}
	
	
}
