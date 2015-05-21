package com.my.hps.webapp.controller.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.ElectricChargeState;
import com.my.hps.webapp.model.ElectricMoneySerializer;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.MoneySerializer;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 电费缴费页面使用的关于电费缴费状态的Bean
 * 
 * @author liuguanjun
 *
 */
public class ElectricChargeStateView {
	
	private HpsHouse house;
	
	private HpsHouseOwner houseOwner;
	
	private String electricChargeStateCode;
	
	private String electricChargeStateName;
	
	private HpsElectricUnit electricUnit;
	
	/**
	 * 余额
	 */
	private double surplus;
	
	/**
	 * 待缴电量合计
	 */
	private long electricAmountSum;
	
	/**
	 * 待缴电费合计
	 */
	private double electricCostSum;
	
	/**
	 * 待缴滞纳金合计 
	 */
	private double zhinaSum;
	
	/**
	 * 卫生费合计
	 */
	private double weishengSum;
	
	/**
	 * 排污费合计
	 */
	private double paiwuSum;
	
	/**
	 * 照明费合计
	 */
	private double zhaomingSum;
	
	/**
	 * 应收：电费 ＋ 各种费除了滞纳金
	 */
	private double mustCharge;
	
	/**
	 * 欠费金额
	 */
	private double qianfei;
	
	/**
	 * 上次余额
	 */
	private double previousSurplus;
	
	private boolean zhinajinOn;
	
	/**
	 * 实际缴纳金额
	 */
	private double actualCharge;
	
	/**
	 * 所有未缴费的抄表记录,包括欠费的和未欠费的,也就是上次开始到本次缴费之间所有的抄表记录
	 * 由于用于存在余额，所以并不是所有的未缴费的抄表记录都是欠费状态
	 */
	private List<HpsElectricChaobiao> unchargedChaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
	
	private Date lastChargeDate;
	
	private Date chargeDate;
	
	public HpsHouse getHouse() {
		return house;
	}

	public String getElectricChargeStateCode() {
		return electricChargeStateCode;
	}

	public String getElectricChargeStateName() {
		return electricChargeStateName;
	}
	
	public HpsElectricUnit getElectricUnit() {
		return electricUnit;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getSurplus() {
		return surplus;
	}

	public static List<ElectricChargeStateView> convertFromList(List<ElectricChargeState> states) {
		List<ElectricChargeStateView> viewStates = new ArrayList<ElectricChargeStateView>();
		for (ElectricChargeState state : states) {
			ElectricChargeStateView viewState = convertFromBean(state);
			viewStates.add(viewState);
		}
		return viewStates;
	}
	
	public static ElectricChargeStateView convertFromBean(ElectricChargeState state) {
		ElectricChargeStateView viewState = new ElectricChargeStateView();
		viewState.electricChargeStateCode = state.getStateEnum().getCode();
		viewState.electricChargeStateName = state.getStateEnum().getName();
		viewState.house = state.getHouse();
		viewState.chargeDate = new Date();
		// 从缴费状态转化时，即是获取最新的缴费状态，所以使用此时的房主信息用于显示
		viewState.houseOwner = state.getHouse().getOwner();
		viewState.surplus = state.getSurplus();
		viewState.unchargedChaobiaoRecords.addAll(state.getSurplusChaobiaoRecords());
		viewState.unchargedChaobiaoRecords.addAll(state.getArrearageChaobiaoRecords());
		viewState.qianfei = viewState.surplus < 0 ? -viewState.surplus : 0d;
		viewState.electricUnit = state.getElectricUnit();
		viewState.zhinajinOn = true;
		HpsElectricChargeRecord lastChargeRecord = state.getLastElectricChargeRecord();
		if (lastChargeRecord != null) {
			viewState.previousSurplus = lastChargeRecord.getCurrentSurplus();
			viewState.lastChargeDate = lastChargeRecord.getChargeDate();
		}
		for (HpsElectricChaobiao chaobiao : viewState.unchargedChaobiaoRecords) {
			// 用电量
			viewState.electricAmountSum += chaobiao.getElectricCount();
			// 电费
			viewState.electricCostSum += chaobiao.getElectricCharge();
			// 滞纳金
			Double zhinajin = chaobiao.getZhinajin();
			if (zhinajin != null) {
				viewState.zhinaSum += chaobiao.getZhinajin();
			}
			// 卫生费
			Double weisheng = chaobiao.getWeishengCharge();
			if (weisheng != null) {
				viewState.weishengSum += chaobiao.getWeishengCharge();
			}
			// 排污费
			Double paiwu = chaobiao.getPaiwuCharge();
			if (paiwu != null) {
				viewState.paiwuSum += chaobiao.getPaiwuCharge();
			}
			// 照明费
			Double zhaoming = chaobiao.getZhaomingCharge();
			if (zhaoming != null) {
				viewState.zhaomingSum += chaobiao.getZhaomingCharge();
			}
		}
		viewState.mustCharge =  viewState.electricCostSum + viewState.weishengSum
				+ viewState.paiwuSum + viewState.zhaomingSum;
		return viewState;
	}
	
	public static ElectricChargeStateView convertFromBean(HpsElectricChargeRecord record) {
		ElectricChargeStateView viewState = new ElectricChargeStateView();
		viewState.electricChargeStateCode = ChargeStateEnum.CHARGED.getCode();
		viewState.electricChargeStateName = ChargeStateEnum.CHARGED.getName();
		viewState.chargeDate = record.getChargeDate();
		// 上次余额
		viewState.previousSurplus = record.getPreviousSurplus();
		viewState.surplus = record.getCurrentSurplus();
		// 此处认为已经缴费的抄表记录都是未缴费状态，这样就可以回溯到缴费时候的状态
		viewState.unchargedChaobiaoRecords.addAll(record.getChaobiaoSet());
		viewState.mustCharge = record.getMustCharge();
		viewState.actualCharge = record.getActualCharge();
		viewState.zhinajinOn = record.isZhinajinOn();
		for (HpsElectricChaobiao chaobiao : viewState.unchargedChaobiaoRecords) {
			if (viewState.house == null) {
				viewState.house = chaobiao.getHouse();
				// 获得抄表时候记录下的房主信息用于显示，因为此时转换属于历史缴费记录的转换
				// 可能存在房主信息变更的情况
				viewState.houseOwner = chaobiao.getHouseOwner();
			}
			if (viewState.electricUnit == null) {
				viewState.electricUnit = chaobiao.getUnit();
			}
			// 用电量
			if (chaobiao.getElectricCount() != null) {
				viewState.electricAmountSum += chaobiao.getElectricCount();
			}
			// 电费
			if (chaobiao.getElectricCharge() != null) {
				viewState.electricCostSum += chaobiao.getElectricCharge();
			}
			// 滞纳金
			Double zhinajin = chaobiao.getZhinajin();
			if (zhinajin != null) {
				viewState.zhinaSum += chaobiao.getZhinajin();
			}
			// 卫生费
			Double weisheng = chaobiao.getWeishengCharge();
			if (weisheng != null) {
				viewState.weishengSum += chaobiao.getWeishengCharge();
			}
			// 排污费
			Double paiwu = chaobiao.getPaiwuCharge();
			if (paiwu != null) {
				viewState.paiwuSum += chaobiao.getPaiwuCharge();
			}
			// 照明费
			Double zhaoming = chaobiao.getZhaomingCharge();
			if (zhaoming != null) {
				viewState.zhaomingSum += chaobiao.getZhaomingCharge();
			}
			chaobiao.setChargeState(ChargeStateEnum.CHARGED);
		}
		return viewState;
	}

	public List<HpsElectricChaobiao> getUnchargedChaobiaoRecords() {
		return unchargedChaobiaoRecords;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Long getElectricAmountSum() {
		return electricAmountSum;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getElectricCostSum() {
		return electricCostSum;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getZhinaSum() {
		return zhinaSum;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getWeishengSum() {
		return weishengSum;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getPaiwuSum() {
		return paiwuSum;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getZhaomingSum() {
		return zhaomingSum;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public double getQianfei() {
		return qianfei;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getMustCharge() {
		return mustCharge;
	}
	
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustChargeToActual() {
		BigDecimal mustChargeToActual = new BigDecimal(qianfei);
		if (zhinajinOn && zhinaSum > 0) {
			mustChargeToActual = mustChargeToActual.add(new BigDecimal(zhinaSum));
		}
		// 电费不收角钱，超出部分滚入余额
		mustChargeToActual = mustChargeToActual.setScale(0, RoundingMode.UP);
		mustChargeToActual = mustChargeToActual.setScale(2);
		return mustChargeToActual.doubleValue();
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getPreviousSurplus() {
		return previousSurplus;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public double getActualCharge() {
		return actualCharge;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastChargeDate() {
		return lastChargeDate;
	}

	public boolean isZhinajinOn() {
		return zhinajinOn;
	}

	public HpsHouseOwner getHouseOwner() {
		return houseOwner;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getChargeDate() {
		return chargeDate;
	}

}
