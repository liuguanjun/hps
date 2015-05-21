package com.my.hps.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.appfuse.service.hps.HpsDictManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;

/**
 * 业务数据字典Controller
 * 
 * @author liuguanjun
 *
 */
@Controller
@RequestMapping("/admin/")
public class DictManagementController extends BaseFormController {
	
	private HpsDictManager dictManager;
	
	@RequestMapping(method=RequestMethod.GET,value="dictitems/{dictCode}")
	@ResponseBody
	public List<HpsDictItem> getDictItems(@PathVariable String dictCode) {
		List<HpsDictItem> items = dictManager.getDictItems(dictCode);
		Collections.sort(items);
		return items;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="dicts")
	@ResponseBody
	public List<HpsDict> getDicts() {
		List<HpsDict> dicts = dictManager.getDicts();
		Collections.sort(dicts);
		for (HpsDict dict : dicts) {
			List<HpsDictItem> items = new ArrayList<HpsDictItem>(dict.getItemSet());
			Collections.sort(items);
			dict.setItemSet(new TreeSet<HpsDictItem>(items));
		}
		return dicts;
	}

	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	

}
