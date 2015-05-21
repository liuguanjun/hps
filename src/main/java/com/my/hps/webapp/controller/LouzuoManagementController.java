package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsLouzuoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.model.HpsLouzuo;
@Controller
@RequestMapping("/admin/LouzuoManagement/")
public class LouzuoManagementController extends BaseFormController {
	
	private HpsLouzuoManager hpsLouzuoManager;

	@Autowired
	public void setHpsLouzuoManager(HpsLouzuoManager hpsLouzuoManager) {
		this.hpsLouzuoManager = hpsLouzuoManager;
	}

	/**
	 * 添加楼座数据
	 * @return HpsLouzuo
	 */
	@RequestMapping(method=RequestMethod.POST,value="louzuo")
	@ResponseBody
	public HpsLouzuo addLouzuo(@ModelAttribute HpsLouzuo louzuo) {
		louzuo = hpsLouzuoManager.saveHpsLouzuo(louzuo);
		return louzuo;
	}
	
	/**
	 * 取得楼座的数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="arealouzuo/{areaId}")
	@ResponseBody
	public List<HpsLouzuo> getLouzuoByAreaId(@PathVariable Long areaId) {
		List<HpsLouzuo> louzuoList = hpsLouzuoManager.getHpsLouzuoByArea(areaId);
		return louzuoList;
	}
	
	/**
	 * 取得楼座的数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="arealouzuo")
	@ResponseBody
	public List<HpsLouzuo> getLouzuoByAreaCode(@RequestParam String areaCode) {
		List<HpsLouzuo> louzuoList = hpsLouzuoManager.getHpsLouzuoByCode(areaCode);
		return louzuoList;
	}
	
	/**
	 * 取得楼座的数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="baselouzuo/{baseId}")
	@ResponseBody
	public List<HpsLouzuo> getLouzuoByBaseId(@PathVariable Long baseId) {
		List<HpsLouzuo> louzuoList = hpsLouzuoManager.getHpsLouzuoByBase(baseId);
		return louzuoList;
	}
	
	/**
	 * 删除被选中的楼座数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.DELETE,value="louzuo/{louzuoId}")
	@ResponseBody
	public List<HpsLouzuo> deleteLouzuo(@PathVariable Long louzuoId) {
//		hpsBaseManager.deleteBaseTreeElement(areaId);
//		List<HpsBase> bases = hpsBaseManager.getBases();
		
		hpsLouzuoManager.deleteLouzuo(louzuoId);
		return null;
	}

}
