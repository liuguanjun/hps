package com.my.hps.webapp.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.service.hps.HpsBaseManager;
import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsElectricUnitManager;
import org.appfuse.service.hps.HpsLouzuoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.ElectricUnitQueryParam;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.HpsLouzuo;
import com.my.hps.webapp.model.PaginationResult;

@Controller
@RequestMapping("/admin/electricUnit/")
public class ElectricUnitController extends BaseFormController {
	
	private HpsElectricUnitManager unitManager;
	private HpsBaseManager baseManager;
	private HpsLouzuoManager louzuoManager;
	private HpsDictManager dictManager;
	
	@RequestMapping(method=RequestMethod.GET,value="units")
	@ResponseBody
	public PaginationResult<HpsElectricUnit> getElectricUnits(@ModelAttribute ElectricUnitQueryParam queryParam) {
		PaginationResult<HpsElectricUnit> result = unitManager.getUnits(queryParam);
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="unit")
	@ResponseBody
	public HpsElectricUnit addElectricUnit(@ModelAttribute HpsElectricUnit unit) {
		String baseCode = unit.getBase().getCode();
		HpsBase baseInDB = baseManager.getBaseByCode(baseCode);
		unit.setBase(baseInDB);
		
		HpsArea areaInput = unit.getArea();
		if (areaInput != null && StringUtils.isNotEmpty(areaInput.getCode())) {
			String areaCodeInput = areaInput.getCode();
			HpsArea areaInDB = null;
			for (HpsArea area : baseInDB.getAreaSet()) {
				if (area.getCode().equals(areaCodeInput)) {
					areaInDB = area;
					break;
				}
			}
			unit.setArea(areaInDB);
		} else {
			unit.setArea(null);
		}
		
		HpsLouzuo louzuoInput = unit.getLouzuo();
		if (louzuoInput != null && StringUtils.isNotEmpty(louzuoInput.getCode())) {
			String louzuoCodeInput = louzuoInput.getCode();
			HpsLouzuo louzuoInDB = null;
			List<HpsLouzuo> areaLouzuos = louzuoManager.getHpsLouzuoByArea(unit.getArea().getId());
			for (HpsLouzuo louzuo : areaLouzuos) {
				if (louzuo.getCode().equals(louzuoCodeInput)) {
					louzuoInDB = louzuo;
					break;
				}
			}
			unit.setLouzuo(louzuoInDB);
		} else {
			unit.setLouzuo(null);
		}
		String yongfangXingzhiCode = unit.getYongfangXingzhi().getCode();
		HpsDictItem yongfangXingzhiItem = dictManager.getYongfangXingzhiDictItem(yongfangXingzhiCode);
		unit.setYongfangXingzhi(yongfangXingzhiItem);
		Double zhinaScale = unit.getZhinaScale();
		if (zhinaScale != null) {
			unit.setZhinaScale(zhinaScale / 1000);
		}
		if (unit.isLevel()) {
			unit.setUnit(null);
		} else {
			unit.setStart1(null);
			unit.setEnd1(null);
			unit.setUnit1(null);
			unit.setStart2(null);
			unit.setEnd2(null);
			unit.setUnit2(null);
			unit.setStart3(null);
			unit.setEnd3(null);
			unit.setUnit3(null);
			unit.setStart4(null);
			unit.setEnd4(null);
			unit.setUnit4(null);
		}
		HpsElectricUnit unitInDB = unitManager.save(unit);
		return unitInDB;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="unit")
	@ResponseBody
	public HpsElectricUnit updateElectricUnit(@ModelAttribute HpsElectricUnit unit) {
		Long unitId =  unit.getId();
		HpsElectricUnit unitInDB = unitManager.get(unitId);
		unitInDB.setHistory(true);
		unitManager.save(unitInDB);
		unit.setId(null);
		return addElectricUnit(unit);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,value="unit/{unitId}")
	@ResponseBody
	public void deleteElectriUnit(@PathVariable Long unitId) {
		HpsElectricUnit unitInDB = unitManager.get(unitId);
		unitInDB.setHistory(true);
		unitManager.save(unitInDB);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="unit/{unitId}")
	@ResponseBody
	public HpsElectricUnit getElectricUnit(@PathVariable Long unitId) {
		HpsElectricUnit unit = unitManager.get(unitId);
		return unit;
	}

	@Autowired
	public void setUnitManager(HpsElectricUnitManager unitManager) {
		this.unitManager = unitManager;
	}
	
	@Autowired
	public void setLouzuoManager(HpsLouzuoManager louzuoManager) {
		this.louzuoManager = louzuoManager;
	}
	
	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}

	@Autowired
	public void setBaseManager(HpsBaseManager baseManager) {
		this.baseManager = baseManager;
	}

}
