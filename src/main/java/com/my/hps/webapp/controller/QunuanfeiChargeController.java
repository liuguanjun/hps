package com.my.hps.webapp.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.appfuse.service.hps.HpsHeatingChargeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Controller
@RequestMapping("/heatingcharge/")
public class QunuanfeiChargeController extends BaseFormController {
	
	private HpsHeatingChargeManager chargeManger;
	
	@RequestMapping(method = RequestMethod.GET, value = "{baseCode}/paymentdates")
	@ResponseBody
	public List<HpsHeatingPaymentDate> getPaymentDates(@PathVariable String baseCode) {
		List<HpsHeatingPaymentDate> paymentDates = chargeManger.getPaymentDates(baseCode);
		return paymentDates;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "chargerecords")
	@ResponseBody
	public PaginationResult<HpsHeatingChargeRecord> getChargeRecords(@ModelAttribute HeatingChargeQueryParam param) {
		PaginationResult<HpsHeatingChargeRecord> result = chargeManger.getChargeRecords(param);
		List<HpsHeatingChargeRecord> records = result.getRows();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		for (HpsHeatingChargeRecord record : records) {
			List<HpsHeatingChargeRecord> divertedRecords = null;
			Double divertedCharge = 0d;
			StringBuilder sb = new StringBuilder();
			if (record.getChargeState() == ChargeStateEnum.CHARGED) {
				// 已缴费的状态，那么获取之前结转的缴费记录
				divertedRecords = chargeManger.getDivertedChargeRecords(record.getId());
			} else if (record.getChargeState() == ChargeStateEnum.UNCHARGED) {
				// 未缴费的状态，获取所有之前没交费的记录
				divertedRecords = chargeManger.getMustDivertChargeRecords(record);
				// 未缴费状态，设置结转金额的初始值（对于系统初始数据导入时，记录之前的结转金额）
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

			for (HpsHeatingChargeRecord divertedRecord : divertedRecords) {
				double mustHeatingCharge = divertedRecord.getMustHeatingCharge();
				sb.append(divertedRecord.getPaymentDate().getTitle());
				sb.append(":");
				sb.append(format.format(mustHeatingCharge));
				sb.append("元    ");
				divertedCharge += mustHeatingCharge;
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
	public HpsHeatingChargeRecord getChargeRecord(@PathVariable Long recordId) {
		HpsHeatingChargeRecord record = chargeManger.getChargeRecord(recordId);
		List<HpsHeatingChargeRecord> divertedRecords = null;
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
			// 未缴费状态，设置结转金额的初始值（对于系统初始数据导入时，记录之前的结转金额）
//			divertedCharge = record.getDivertedCharge();
//			if (divertedCharge == null) {
//				divertedCharge = 0d;
//			} else if (divertedCharge > 0) {
//				sb.append("初始历年结转：" + format.format(divertedCharge) + "元");
//			}
		} else {
			return record;
		}
		for (HpsHeatingChargeRecord divertedRecord : divertedRecords) {
			double mustHeatingCharge = divertedRecord.getMustHeatingCharge();
			sb.append(divertedRecord.getPaymentDate().getTitle());
			sb.append(":");
			sb.append(format.format(mustHeatingCharge));
			sb.append("元    ");
			divertedCharge += mustHeatingCharge;
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
	public HpsHeatingChargeRecord charge(@PathVariable Long recordId, 
				@RequestParam Double actualSumCharge,
				@RequestParam(required = false, defaultValue = "false") boolean zhinajinOff,
				@RequestParam(required = false, defaultValue = "false") boolean stopped,
				@RequestParam(required = false, defaultValue = "false") boolean livingSohard,
				@RequestParam(required = false, defaultValue = "") String wageNum,
				@RequestParam(required = false, defaultValue = "") String remarks) {
		HpsHeatingChargeRecord record = chargeManger.charge(recordId, actualSumCharge,
				zhinajinOff, stopped, livingSohard, wageNum, remarks);
		return record;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{recordId}/cancel")
	@ResponseBody
	public HpsHeatingChargeRecord cancel(@PathVariable Long recordId, 
				@RequestParam(required = false, defaultValue = "") String remarks) {
		HpsHeatingChargeRecord record = chargeManger.cancel(recordId, remarks);
		return record;
	}

	@Autowired
	public void setChargeManger(HpsHeatingChargeManager chargeManger) {
		this.chargeManger = chargeManger;
	}
	
	
	
}
