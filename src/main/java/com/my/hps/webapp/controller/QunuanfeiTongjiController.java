package com.my.hps.webapp.controller;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.hps.HpsQunuanfeiTongjiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.QunuanfeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsQunuanfeiTongjiView;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsUser;

@Controller
@RequestMapping("/getQunuanfeiTongji/")
public class QunuanfeiTongjiController extends BaseFormController {
	
	private HpsQunuanfeiTongjiManager qunuanfeiTongjiManager;
	
	@Autowired
	public void setQunuanfeiTongjiManager(
			HpsQunuanfeiTongjiManager qunuanfeiTongjiManager) {
		this.qunuanfeiTongjiManager = qunuanfeiTongjiManager;
	}

	@RequestMapping(method = RequestMethod.GET, value = "caozuoyuan/{baseCode}")
	@ResponseBody
	public List<HpsUser> getCaozuoyuan(@PathVariable String baseCode) {
		List<HpsUser> caozuoyuanList = qunuanfeiTongjiManager.getCaozuoyuan(baseCode);
		return caozuoyuanList;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "qunuanfeiTongji/{baseCode}")
	@ResponseBody
	public List<HpsQunuanfeiTongjiView> getQunuanfeiTongjiInfo(@ModelAttribute QunuanfeiTongjiQueryParam param) {
		List<HpsQunuanfeiTongjiView> qunuanfeiTongjiViewList = new ArrayList<HpsQunuanfeiTongjiView>();
		HpsQunuanfeiTongjiView qunuanfeiTongjiView = new HpsQunuanfeiTongjiView();
		// 取得收费户数
		qunuanfeiTongjiManager.getQunuanfeiTongjiHushu(qunuanfeiTongjiView, param);
		// 计划总额,实缴取暖费总额
		qunuanfeiTongjiManager.getQunuanfeiTongjiJihuaAndHushu(qunuanfeiTongjiView, param);
		// 取得全额收费(例),全额收费额
		qunuanfeiTongjiManager.getQunuanfeiTongjiQuanE(qunuanfeiTongjiView, param);
		// 取得惠后收费(例),惠后收费额
		qunuanfeiTongjiManager.getQunuanfeiTongjiHuihou(qunuanfeiTongjiView, param);
		// 取得停供收费(例),停供收费额
		qunuanfeiTongjiManager.getQunuanfeiTongjiTinggong(qunuanfeiTongjiView, param);
		// 取得困难住户收费(例),困难住户收费额
		qunuanfeiTongjiManager.getQunuanfeiTongjiKunnan(qunuanfeiTongjiView, param);
		// 取得滞纳缴费(例),滞纳缴费额
		qunuanfeiTongjiManager.getQunuanfeiTongjiZhinajin(qunuanfeiTongjiView, param);
		// 取得优惠减免额
		qunuanfeiTongjiManager.getQunuanfeiTongjiYouhuiJianmian(qunuanfeiTongjiView, param);
		// 取得停供减免额
		qunuanfeiTongjiManager.getQunuanfeiTongjiTinggongJianmian(qunuanfeiTongjiView, param);
		// 取得困难住户减免额
		qunuanfeiTongjiManager.getQunuanfeiTongjiKunnanJianmian(qunuanfeiTongjiView, param);
		// 取得总减免额
		Double youhuiJianmian = qunuanfeiTongjiView.getYouhuiJianmianChargeDouble();
		Double tinggongJianmian = qunuanfeiTongjiView.getTinggongJianmianChargeDouble();
		Double kunnanJianmian = qunuanfeiTongjiView.getKunnanJianmianChargeDouble();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		qunuanfeiTongjiView.setJianmianAllCharge(format.format((youhuiJianmian==null?0:youhuiJianmian) 
				+ (tinggongJianmian==null?0:tinggongJianmian) + (kunnanJianmian==null?0:kunnanJianmian)));
		qunuanfeiTongjiViewList.add(qunuanfeiTongjiView);
		return qunuanfeiTongjiViewList;
	}
}
