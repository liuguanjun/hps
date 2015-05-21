package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsQunuanfeiUnitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;

@Controller
@RequestMapping("/qunuanfeiUnit/")
public class QunuanfeiUnitManagementController extends BaseFormController {
	private HpsQunuanfeiUnitManager QunuanfeiUnitManager;
	private HpsDictManager dictManager;
	
	@Autowired
	public void setQunuanfeiUnitManager(
			HpsQunuanfeiUnitManager QunuanfeiUnitManager) {
		this.QunuanfeiUnitManager = QunuanfeiUnitManager;
	}
	
	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	/**
	 * 	取得取暖费单价
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getQunuanfeiUnit")
	@ResponseBody
	public List<HpsHeatingUnit> getQunuanfeiUnit(@ModelAttribute QunuanfeiDiscountQueryParam param) {
		List<HpsHeatingUnit> heatingUnitList = QunuanfeiUnitManager.getQunuanfeiUnit(param);
		return heatingUnitList;
	}
	
	/**
	 * 	取得要修改的取暖费单价
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="getQunuanfeiUnitById/{UnitId}")
	@ResponseBody
	public List<HpsHeatingUnit> getQunuanfeiUnitById(@PathVariable Long UnitId) {
		List<HpsHeatingUnit> heatingUnitList = QunuanfeiUnitManager.getQunuanfeiUnitById(UnitId);
		return heatingUnitList;
	}
	
	/**
	 * 	添加取暖费单价
	 * @return HpsHeatingUnit
	 */
	@RequestMapping(method=RequestMethod.POST,value="addQunuanfeiUnit")
	@ResponseBody
	public HpsHeatingUnit addQunuanfeiUnit(@ModelAttribute HpsHeatingUnit heatingUnit) {
		setCodeItems(heatingUnit);
		HpsHeatingUnit heatingUnitRtn;
		if (heatingUnit.getId() == null) {
			heatingUnitRtn = QunuanfeiUnitManager.addQunuanfeiUnit(heatingUnit);
		} else {
			heatingUnitRtn = QunuanfeiUnitManager.updateQunuanfeiUnit(heatingUnit);
		}
		return heatingUnitRtn;
	}
	
	/**
	 * 通过code用房性质并重新设置到heatingUnit中，
	 */
	private void setCodeItems(HpsHeatingUnit heatingUnit) {
		// 用房性质
		String yongfangxingzhiCode =heatingUnit.getYongfangXingzhi().getCode();
		HpsDictItem yongfangxingzhiItem = dictManager.getYongfangXingzhiDictItem(yongfangxingzhiCode);
		heatingUnit.setYongfangXingzhi(yongfangxingzhiItem);
	}

}
