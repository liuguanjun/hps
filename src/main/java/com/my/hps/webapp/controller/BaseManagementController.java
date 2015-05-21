package com.my.hps.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.hps.HpsBaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.vo.HpsBaseTreeElement;
import com.my.hps.webapp.controller.vo.TreeElement;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;


@Controller
@RequestMapping("/admin/BaseManagement/")
public class BaseManagementController extends BaseFormController {
	private HpsBaseManager hpsBaseManager;
	
	/**
	 * 取得基地和区域的树形结构的数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="baseTreeElements")
	@ResponseBody
	public List<HpsBaseTreeElement> getBaseTreeElements() {
		List<HpsBase> bases = hpsBaseManager.getBases();
		return convertBases(bases);
	}
	
	/**
	 * 删除被选中的区域数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.DELETE,value="baseTreeElements/{areaId}")
	@ResponseBody
	public List<HpsBaseTreeElement> deleteBaseTreeElement(@PathVariable Long areaId) {
		hpsBaseManager.deleteBaseTreeElement(areaId);
		List<HpsBase> bases = hpsBaseManager.getBases();		
		return convertBases(bases);
	}
	
	/**
	 * 取得基地的树形结构的数据
	 * @return List
	 */
	@RequestMapping(method=RequestMethod.GET,value="baseTree")
	@ResponseBody
	public List<HpsBase> getBaseTree() {
		List<HpsBase> bases = hpsBaseManager.getBases();
		return bases;
	}
	
	/**
	 * 添加区域数据
	 * @return HpsArea
	 */
	@RequestMapping(method=RequestMethod.POST,value="area")
	@ResponseBody
	public HpsArea addArea(@ModelAttribute HpsArea area) {
		HpsBase hpsBaseTransient = area.getBase();
		HpsBase hpsBaseInDB = hpsBaseManager.getBaseById(hpsBaseTransient.getId());
		area.setBase(hpsBaseInDB);
		area = hpsBaseManager.saveHpsArea(area);
		return area;
	}
	
	@Autowired
	public void setBaseManager(HpsBaseManager baseManager) {
		this.hpsBaseManager = baseManager;
	}
	
	private List<HpsBaseTreeElement> convertBases(List<HpsBase> bases) {
		List<HpsBaseTreeElement> treeElements = new ArrayList<HpsBaseTreeElement>();
		for (HpsBase base : bases) {
			HpsBaseTreeElement treeElement = new HpsBaseTreeElement();
			Long baseId = base.getId();
			treeElement.setId(baseId.toString());
			treeElement.setBizzDataId(baseId.toString());
			treeElement.setName(base.getName());
			treeElement.setCode(base.getCode());
			treeElement.setSystemInner(base.isSystemInner() ? "系统内置" : "用户自定义");
			treeElement.setBase(true);
			treeElement.setBaseCode(base.getCode());
			List<TreeElement> children = new ArrayList<TreeElement>();
			for (HpsArea childArea : base.getAreaSet()) {
				HpsBaseTreeElement childElement = new HpsBaseTreeElement();
				Long areaId = childArea.getId();
				childElement.setId(baseId * 1000 + areaId + "");
				childElement.setBizzDataId(areaId.toString());
				childElement.setName(childArea.getName());
				childElement.setCode(childArea.getCode());
				childElement.setBaseCode(base.getCode());
				childElement.setSystemInner(childArea.isSystemInner() ? "系统内置" : "用户自定义");
				children.add(childElement);
			}
			treeElement.setChildren(children);
			treeElements.add(treeElement);
		}
		return treeElements;
	}
	
//	private List<HpsBase> removeBaseTreeElements(List<HpsBase> bases, Long areaId) {
//		for (HpsBase base : bases) {
//			Set areaElements = base.getAreaSet();
//			Iterator iterator = areaElements.iterator();
//			while (iterator.hasNext()) {
//				HpsArea childArea = (HpsArea)iterator.next();
//				if (childArea.getId().equals(areaId)) {
//					iterator.remove();
//				}
//			}
//		}
//		return bases;
//	}
}
