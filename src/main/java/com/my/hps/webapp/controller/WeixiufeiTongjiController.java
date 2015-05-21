package com.my.hps.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.hps.HpsWeixiufeiTongjiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.WeixiufeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsWeixiufeiTongjiView;
import com.my.hps.webapp.controller.vo.HpsWeixiufeiTongjiView;
import com.my.hps.webapp.model.HpsUser;
@Controller
@RequestMapping("/getWeixiufeiTongji/")
public class WeixiufeiTongjiController extends BaseFormController {
	
	private HpsWeixiufeiTongjiManager weixiufeiTongjiManager;
	
	@Autowired
	public void setWeixiufeiTongjiManager(
			HpsWeixiufeiTongjiManager weixiufeiTongjiManager) {
		this.weixiufeiTongjiManager = weixiufeiTongjiManager;
	}

	@RequestMapping(method = RequestMethod.GET, value = "caozuoyuan/{baseCode}")
	@ResponseBody
	public List<HpsUser> getCaozuoyuan(@PathVariable String baseCode) {
		List<HpsUser> caozuoyuanList = weixiufeiTongjiManager.getCaozuoyuan(baseCode);
		return caozuoyuanList;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "weixiufeiTongji/{baseCode}")
	@ResponseBody
	public List<HpsWeixiufeiTongjiView> getWeixiufeiTongjiInfo(@ModelAttribute WeixiufeiTongjiQueryParam param) {
		List<HpsWeixiufeiTongjiView> weixiufeiTongjiViewList = new ArrayList<HpsWeixiufeiTongjiView>();
		HpsWeixiufeiTongjiView weixiufeiTongjiView = new HpsWeixiufeiTongjiView();
		// 取得收费户数,计划维修费总额,实缴维修费总额
		weixiufeiTongjiManager.getWeixiufeiTongjiHushu(weixiufeiTongjiView, param);
		weixiufeiTongjiManager.getWeixiufeiTongjiJihuaAndHushu(weixiufeiTongjiView, param);
		weixiufeiTongjiViewList.add(weixiufeiTongjiView);
		return weixiufeiTongjiViewList;
	}
}
