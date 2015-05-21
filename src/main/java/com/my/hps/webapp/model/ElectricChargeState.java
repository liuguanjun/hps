package com.my.hps.webapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.cxf.common.util.CollectionUtils;

import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 电费缴费状态
 * 
 * @author liuguanjun
 *
 */
public class ElectricChargeState {
	
	private HpsHouse house;
	
	/**
	 * 记录单价，
	 * 虽然单价在每一个抄表记录中记录，但是此处用于记录当前房屋的电价，即：最新电价
	 */
	private HpsElectricUnit electricUnit;
	
	/**
	 * 所有的缴费记录
	 */
	private List<HpsElectricChargeRecord> chargeRecords = new ArrayList<HpsElectricChargeRecord>();
	
	/**
	 * 所有的抄表记录
	 */
	private List<HpsElectricChaobiao> chaobiaoRecords;
	
	/**
	 * 欠费的抄表记录
	 */
	private List<HpsElectricChaobiao> arrearageChaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
	
	/**
	 * 未欠费，但是未缴费的抄表记录，即：余额超出部分的抄表记录
	 */
	private List<HpsElectricChaobiao> surplusChaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
	
	/**
	 * 已经缴费的抄表记录
	 */
	private List<HpsElectricChaobiao> chargedChaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
	
	/**
	 * 余额
	 */
	private Double surplus = 0d;
	
	public ElectricChargeState(HpsHouse house) {
		super();
		this.house = house;
	}
	public HpsHouse getHouse() {
		return house;
	}
	public List<HpsElectricChargeRecord> getChargeRecords() {
		return chargeRecords;
	}
	public List<HpsElectricChaobiao> getChaobiaoRecords() {
		return chaobiaoRecords;
	}
	public void setChaobiaoRecords(List<HpsElectricChaobiao> chaobiaoRecords) {
		// 过滤掉房屋不匹配的抄表记录
		this.chaobiaoRecords = new ArrayList<HpsElectricChaobiao>();
		for (HpsElectricChaobiao chaobiao : chaobiaoRecords) {
			if (chaobiao.getHouse().getId().equals(house.getId())) {
				this.chaobiaoRecords.add(chaobiao);
			}
		}
		Collections.sort(this.chaobiaoRecords, new Comparator<HpsElectricChaobiao>() {

			@Override
			public int compare(HpsElectricChaobiao o1,
					HpsElectricChaobiao o2) {
				if (o1.id == o2.id) {
					return 0;
				} else {
					// 收取月份正序
					return o1.getPaymentDate().getMonth().compareTo(o2.getPaymentDate().getMonth());
				}
			}
		});
	}
	
	/**
	 * 所有的欠费的抄表记录
	 * 
	 * @return
	 */
	public List<HpsElectricChaobiao> getArrearageChaobiaoRecords() {
		return arrearageChaobiaoRecords;
	}
	
	/**
	 * 没有欠费，但是还没有办理缴费的抄表记录，适用于户主余额有剩余的情况
	 * 
	 * @return
	 */
	public List<HpsElectricChaobiao> getSurplusChaobiaoRecords() {
		return surplusChaobiaoRecords;
	}
	
	/**
	 * 最近的一次缴费记录
	 * 
	 * @return
	 */
	public HpsElectricChargeRecord getLastElectricChargeRecord() {
		if (CollectionUtils.isEmpty(chargeRecords)) {
			return null;
		} else {
			return chargeRecords.get(0);
		}
	}
	
	private double caculateChargeValue(HpsElectricChaobiao chaobiao, boolean zhinajinOn) {
		// 本次抄表应该缴纳的金额
		// 电费 + 排污费 + 卫生费 + 照明费 + 滞纳金
		double chaobiaoSum = chaobiao.getElectricCharge() + 
				chaobiao.getPaiwuCharge() + chaobiao.getWeishengCharge() +
				chaobiao.getZhaomingCharge() + (zhinajinOn ? chaobiao.getZhinajin() : 0);
		return chaobiaoSum;
	}
	
	public void caculate(boolean zhinajinOn) {
		// 得到所有的缴费记录
		for (HpsElectricChaobiao chaobiao : chaobiaoRecords) {
			HpsElectricChargeRecord chargeRecord = chaobiao.getChargeRecord();
			if (chargeRecord != null) {
				chargeRecords.add(chargeRecord);
			}
		}
		try {
			Collections.sort(this.chargeRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 最近一次的缴费记录
		HpsElectricChargeRecord lastCharge = getLastElectricChargeRecord();
		// 用户余额
		Double surplus = 0d;
		if (lastCharge != null) {
			surplus = lastCharge.getCurrentSurplus();
		}
		for (HpsElectricChaobiao chaobiao : chaobiaoRecords) {
			if (chaobiao.getChargeRecord() != null) {
				// 有消费记录，证明已经缴费
				chargedChaobiaoRecords.add(chaobiao);
				chaobiao.setChargeState(ChargeStateEnum.CHARGED);
			} else {
				if (electricUnit == null) { // 记录当前单价，不特意获取最新的单价，因为电费单价基本稳定
					electricUnit = chaobiao.getUnit();
				}
				Double chaobiaoSum = caculateChargeValue(chaobiao, zhinajinOn);
				surplus -= chaobiaoSum;
				BigDecimal bigDecimal = new BigDecimal(surplus);
				bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
				surplus = bigDecimal.doubleValue();
				if (surplus < 0) {
					arrearageChaobiaoRecords.add(chaobiao);
					chaobiao.setChargeState(ChargeStateEnum.UNCHARGED);
				} else {
					surplusChaobiaoRecords.add(chaobiao);
					chaobiao.setChargeState(ChargeStateEnum.CHARGED);
				}
			}
		}
		this.surplus = surplus;
	}
	
	public HpsElectricUnit getElectricUnit() {
		return electricUnit;
	}
	public void setElectricUnit(HpsElectricUnit electricUnit) {
		this.electricUnit = electricUnit;
	}
	
	public Double getSurplus() {
		return surplus;
	}
	
	public ChargeStateEnum getStateEnum() {
		if (surplus < 0) {
			return ChargeStateEnum.UNCHARGED;
		} else {
			return ChargeStateEnum.CHARGED;
		}
	}

}
