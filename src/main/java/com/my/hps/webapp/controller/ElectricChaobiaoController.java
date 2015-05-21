package com.my.hps.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.appfuse.service.hps.HpsElectricChaobiaoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.my.hps.webapp.controller.queryparam.ChaobiaoSaveParam;
import com.my.hps.webapp.controller.queryparam.ElectricChaobiaoQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoPaginationResult;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoViewPaginationResult;
import com.my.hps.webapp.controller.vo.HpsElectricChaobiaoView;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;

@Controller
@RequestMapping("/admin/ElectricManagement/")
public class ElectricChaobiaoController {
	
	private HpsElectricChaobiaoManager hpselectricmanager;

	/**
	 * 获取抄表记录
	 * @param param
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="houseOfElectric")
	@ResponseBody
	public ElectricChaobiaoViewPaginationResult getHouseOfElectric(@ModelAttribute ElectricChaobiaoQueryParam param) {
	    ElectricChaobiaoPaginationResult chaobiaoResult = hpselectricmanager.getChaobiaos(param);
		ElectricChaobiaoViewPaginationResult chaobiaoViewResult = new ElectricChaobiaoViewPaginationResult();
		List<HpsElectricChaobiao> chaobiaos = chaobiaoResult.getRows();
		List<HpsElectricChaobiaoView> chaobiaoViews = new ArrayList<HpsElectricChaobiaoView>();
		for (HpsElectricChaobiao chaobiao : chaobiaos) {
			HpsElectricChaobiaoView chaobiaoView = convertToChaobiaoView(chaobiao);
			chaobiaoViews.add(chaobiaoView);
		}
		chaobiaoViewResult.setRows(chaobiaoViews);
		chaobiaoViewResult.setTotal(chaobiaoResult.getTotal());
		chaobiaoViewResult.setYingshouElectricCount(chaobiaoResult.getYingshouElectricCount());
		chaobiaoViewResult.setYingshouElectricCharge(chaobiaoResult.getYingshouElectricCharge());
		chaobiaoViewResult.setYishouElectricCount(chaobiaoResult.getYishouElectricCount());
		chaobiaoViewResult.setYishouElectricCharge(chaobiaoResult.getYishouElectricCharge());
		return chaobiaoViewResult;
	}
	
	/**
	 * 保存/更改抄表记录
	 * @param param
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="saveElectricChaobiao")
	@ResponseBody
	public List<HpsElectricChaobiaoView> saveElectricChaobiao(@ModelAttribute ChaobiaoSaveParam param) {
		List<HpsElectricChaobiaoView> listChangeRow = JSON.parseArray(param.getUpdated(), HpsElectricChaobiaoView.class);
		List<HpsElectricChaobiao> updateChangeRows = new ArrayList<HpsElectricChaobiao>();
		if (CollectionUtils.isEmpty(listChangeRow)) {
			return Collections.emptyList();
		}
		// 用来存储需要修改的数据的条件
		for (HpsElectricChaobiaoView hpsElectricityView : listChangeRow) {
			HpsElectricChaobiao electricityChaobiao = convertElectriChaobiao(hpsElectricityView);
			HpsElectricChaobiao chaobiaoInDB = hpselectricmanager.get(electricityChaobiao.getId());
			// 本期表值
			chaobiaoInDB.setReadoutsElectric(electricityChaobiao.getReadoutsElectric());
			// 上期表值
			Long provReadout = electricityChaobiao.getProvReadoutsElectric();
			if (provReadout != null) {
				chaobiaoInDB.setProvReadoutsElectric(provReadout);
			}
			// 表变更超值
			chaobiaoInDB.setNewReadoutsElectric(electricityChaobiao.getNewReadoutsElectric());
			updateChangeRows.add(chaobiaoInDB);
		}
		hpselectricmanager.updateElectricChaobiao(updateChangeRows);
		return listChangeRow;
	}
	
	private HpsElectricChaobiaoView convertToChaobiaoView(HpsElectricChaobiao electricChaobiao) {
		HpsElectricChaobiaoView electricityView = new HpsElectricChaobiaoView();
		electricityView.setId(electricChaobiao.getId());// 计费ID
		electricityView.setHourseNo(electricChaobiao.getHouse().getNo()); // 户号
		electricityView.setHourseId(electricChaobiao.getHouse().getId()); // 户号ID
		electricityView.setHouseOwnerName(electricChaobiao.getHouse().getOwner().getName());// 户名
		electricityView.setAddress(electricChaobiao.getHouse().getShortAddress());// 地址 
		Long previousReadout = electricChaobiao.getProvReadoutsElectric();
		if (previousReadout != null) {
			electricityView.setProvReadoutsElectric(previousReadout.toString());
		}
		Long readout = electricChaobiao.getReadoutsElectric();
		if (readout != null) {
			electricityView.setReadoutsElectric(readout.toString());// 本期表值
		}
		Long newReadout = electricChaobiao.getNewReadoutsElectric();
		if (newReadout != null) {
			electricityView.setNewReadoutsElectric(newReadout.toString());// 表变更抄值
		}
		electricityView.setReadMeterDate(electricChaobiao.getReadMeterDate());
		// 基地
		electricityView.setBase(electricChaobiao.getHouse().getLouzuo().getArea().getBase().getCode());
		// 区域
		electricityView.setArea(electricChaobiao.getHouse().getLouzuo().getArea().getCode());
		// 楼座
		electricityView.setLouzuo(electricChaobiao.getHouse().getLouzuo().getCode());
		// 单元
		electricityView.setDanyuan(electricChaobiao.getHouse().getDanyuan());
		// 楼层
		electricityView.setCeng(electricChaobiao.getHouse().getCeng());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		electricityView.setChargeMonth(format.format(electricChaobiao.getPaymentDate().getMonth()));
		// 缴费日期
		HpsElectricChargeRecord chargeRecord = electricChaobiao.getChargeRecord();
		if (chargeRecord != null) {
			electricityView.setChargeDate(chargeRecord.getChargeDate());
		}
		return electricityView;
	}

	/**
	 * 将画面上的抄表记录view转换成抄表实体
	 * @param hpsElectricityView
	 * @return
	 */
	private HpsElectricChaobiao convertElectriChaobiao(HpsElectricChaobiaoView hpsElectricityView) {
		HpsElectricChaobiao electricityChaobiao = new HpsElectricChaobiao();
		electricityChaobiao.setId(hpsElectricityView.getId());// 抄表ID
		// 上期表值
		String provReadoutsElectricStr = hpsElectricityView.getProvReadoutsElectric();
		Long provReadoutsElectric = null;
		if (StringUtils.isNotEmpty(provReadoutsElectricStr)) {
			provReadoutsElectric = Long.parseLong(provReadoutsElectricStr);
		}
		electricityChaobiao.setProvReadoutsElectric(provReadoutsElectric);
		// 本期表值
		String readoutStr = hpsElectricityView.getReadoutsElectric();;
		Long readoutsElectric = null;
		if (StringUtils.isNotEmpty(readoutStr)) {
			readoutsElectric = Long.parseLong(readoutStr);
		}
		electricityChaobiao.setReadoutsElectric(readoutsElectric);// 本期表值
		String newReadoutStr = hpsElectricityView.getNewReadoutsElectric();
		Long newReadoutsElectric = null;
		if (StringUtils.isNotEmpty(newReadoutStr)) {
			newReadoutsElectric = Long.parseLong(newReadoutStr);
		}
		electricityChaobiao.setNewReadoutsElectric(newReadoutsElectric);// 表变更值
		return electricityChaobiao;
	}

	@Autowired
	public void setHpselectricmanager(HpsElectricChaobiaoManager hpselectricmanager) {
		this.hpselectricmanager = hpselectricmanager;
	}
}
