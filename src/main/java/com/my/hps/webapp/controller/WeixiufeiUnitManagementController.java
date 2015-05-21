package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsWeixiufeiUnitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.WeixiufeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsMaintainUnit;

@Controller
@RequestMapping("/weixiufeiUnit/")
public class WeixiufeiUnitManagementController extends BaseFormController {
	private HpsWeixiufeiUnitManager WeixiufeiUnitManager;
	private HpsDictManager dictManager;
	
	@Autowired
	public void setWeixiufeiUnitManager(
			HpsWeixiufeiUnitManager WeixiufeiUnitManager) {
		this.WeixiufeiUnitManager = WeixiufeiUnitManager;
	}
	
	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	/**
	 * 	取得维修费单价
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getWeixiufeiUnit")
	@ResponseBody
	public List<HpsMaintainUnit> getWeixiufeiUnit(@ModelAttribute WeixiufeiDiscountQueryParam param) {
		List<HpsMaintainUnit> weixiufeiUnitList = WeixiufeiUnitManager.getWeixiufeiUnit(param);
		return weixiufeiUnitList;
	}
	
	/**
	 * 	取得要修改的维修费单价
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getWeixiufeiUnitById/{UnitId}")
	@ResponseBody
	public List<HpsMaintainUnit> getWeixiufeiUnitById(@PathVariable Long UnitId) {
		List<HpsMaintainUnit> weixiufeiUnitList = WeixiufeiUnitManager.getWeixiufeiUnitById(UnitId);
		return weixiufeiUnitList;
	}
	
	/**
	 * 	添加维修费单价
	 * @return HpsMaintainUnit
	 */
	@RequestMapping(method=RequestMethod.POST,value="addWeixiufeiUnit")
	@ResponseBody
	public HpsMaintainUnit addWeixiufeiUnit(@ModelAttribute HpsMaintainUnit weixiufeiUnit) {
		setCodeItems(weixiufeiUnit);
		HpsMaintainUnit weixiufeiUnitRtn;
		if (weixiufeiUnit.getId() == null) {
			weixiufeiUnitRtn = WeixiufeiUnitManager.addWeixiufeiUnit(weixiufeiUnit);
		} else {
			weixiufeiUnitRtn = WeixiufeiUnitManager.updateWeixiufeiUnit(weixiufeiUnit);
		}
		return weixiufeiUnitRtn;
	}
	
	/**
	 * 通过code用房性质并重新设置到weixiufeiUnit中，
	 */
	private void setCodeItems(HpsMaintainUnit weixiufeiUnit) {
		// 用房性质
		String yongfangxingzhiCode =weixiufeiUnit.getYongfangXingzhi().getCode();
		HpsDictItem yongfangxingzhiItem = dictManager.getYongfangXingzhiDictItem(yongfangxingzhiCode);
		weixiufeiUnit.setYongfangXingzhi(yongfangxingzhiItem);
	}

}
