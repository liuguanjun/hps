package com.my.hps.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.enums.BaseEnumJsonSerializer;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 电费
 *
 */
@Entity
@Table(name = "hps_electric_chaobiao")
public class HpsElectricChaobiao extends HpsBaseObject implements Serializable {
	
	private static final long serialVersionUID = -5433339869437495243L;

	/**
	 * 电费单价，电费设计中只存在一份有效的电费单价的定义，但是电价不是一成不变的
	 * 所以此字段的值为抄表数据录入时的电费单价。在电费单价修改之后，接下来月份的
	 * 抄表记录就会记录新的电费单价。
	 * 在电费修改之后，会将之前的电价修改为历史电价，电费单价维护页面只能看到有效的
	 * 电费单价定义
	 */
	private HpsElectricUnit unit;
	
	/**
	 * 上期表值
	 */
	private Long provReadoutsElectric;
	
	/**
	 * 表值
	 */
	private Long readoutsElectric;
	
	/**
	 * 表变更值
	 */
	private Long newReadoutsElectric;
	
	/**
	 * 用电量
	 */
	private Long electricCount;
	
	/**
	 * 电费
	 */
	private Double electricCharge;
	
	/**
	 * 卫生费
	 */
	private Double weishengCharge;
	
	/**
	 * 照明费
	 */
	private Double zhaomingCharge;
	
	/**
	 * 排污费
	 */
	private Double paiwuCharge;
	
	/**
	 * 滞纳金
	 */
	private Double zhinajin;
	
	/**
	 * 逾期天数
	 */
	private Integer zhinajinDayCount;
	
	/**
	 * 抄表日期
	 */
	private Date readMeterDate;
	
	/**
	 * 房屋
	 */
	private HpsHouse house;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner houseOwner;
	
	/**
	 * 缴费记录
	 */
	private HpsElectricChargeRecord chargeRecord;
	
	/**
	 * 对应的缴费日期
	 */
	private HpsElectricPaymentDate paymentDate;
	
	/**
	 * 记录本条抄表记录的缴费状态,不持久化到数据库，通过调用
	 * ElectricChargeState的caculate方法可以得到是否缴费的状态
	 */
	private ChargeStateEnum chargeState;

	@Column(name = "zhinajin_day_count")
	public Integer getZhinajinDayCount() {
		return zhinajinDayCount;
	}

	public void setZhinajinDayCount(Integer zhinajinDayCount) {
		this.zhinajinDayCount = zhinajinDayCount;
	}

	@Column(name = "readoutsElectric", length = 50)
	public Long getReadoutsElectric() {
		return readoutsElectric;
	}

	public void setReadoutsElectric(Long readoutsElectric) {
		this.readoutsElectric = readoutsElectric;
	}

	@Column(name = "newReadoutsElectric", length = 50)
	public Long getNewReadoutsElectric() {
		return newReadoutsElectric;
	}

	public void setNewReadoutsElectric(Long newReadoutsElectric) {
		this.newReadoutsElectric = newReadoutsElectric;
	}

	@Column(name = "readMeterDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getReadMeterDate() {
		return readMeterDate;
	}

	public void setReadMeterDate(Date readMeterDate) {
		this.readMeterDate = readMeterDate;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "house_id")
	public HpsHouse getHouse() {
		return house;
	}

	public void setHouse(HpsHouse house) {
		this.house = house;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "house_owner_id")
	@JsonIgnore
	public HpsHouseOwner getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(HpsHouseOwner houseOwner) {
		this.houseOwner = houseOwner;
	}
	
	@Column(nullable = true, name = "provReadoutsElectric")
	public Long getProvReadoutsElectric() {
		return provReadoutsElectric;
	}

	public void setProvReadoutsElectric(Long provReadoutsElectric) {
		this.provReadoutsElectric = provReadoutsElectric;
	}

	@Column(nullable = true, name = "electricCount")
	public Long getElectricCount() {
		return electricCount;
	}

	public void setElectricCount(Long electricCount) {
		this.electricCount = electricCount;
	}

	@Column(nullable = true, name = "electricCharge")
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getElectricCharge() {
		return electricCharge;
	}

	public void setElectricCharge(Double electricCharge) {
		this.electricCharge = electricCharge;
	}

	@Column(nullable = true, name = "weishengCharge")
	public Double getWeishengCharge() {
		return weishengCharge;
	}

	public void setWeishengCharge(Double weishengCharge) {
		this.weishengCharge = weishengCharge;
	}

	@Column(nullable = true, name = "zhaomingCharge")
	public Double getZhaomingCharge() {
		return zhaomingCharge;
	}

	public void setZhaomingCharge(Double zhaomingCharge) {
		this.zhaomingCharge = zhaomingCharge;
	}

	@Column(nullable = true, name = "paiwuCharge")
	public Double getPaiwuCharge() {
		return paiwuCharge;
	}

	public void setPaiwuCharge(Double paiwuCharge) {
		this.paiwuCharge = paiwuCharge;
	}

	@Column(nullable = true, name = "zhinajin")
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getZhinajin() {
		return zhinajin;
	}

	public void setZhinajin(Double zhinajin) {
		this.zhinajin = zhinajin;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "charge_record_id")
	@JsonIgnore
	public HpsElectricChargeRecord getChargeRecord() {
		return chargeRecord;
	}

	public void setChargeRecord(HpsElectricChargeRecord chargeRecord) {
		this.chargeRecord = chargeRecord;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsElectricPaymentDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsElectricPaymentDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "unit_id")
	public HpsElectricUnit getUnit() {
		return unit;
	}

	public void setUnit(HpsElectricUnit unit) {
		this.unit = unit;
	}

	@Transient
	@JsonSerialize(using = BaseEnumJsonSerializer.class)
	public ChargeStateEnum getChargeState() {
		return chargeState;
	}

	public void setChargeState(ChargeStateEnum chargeState) {
		this.chargeState = chargeState;
	}

	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", this.id)
		.append("provReadoutsElectric", this.provReadoutsElectric)
		.append("readoutsElectric", this.readoutsElectric)
		.append("newReadoutsElectric", this.newReadoutsElectric)
		.append("electricCount", this.electricCount)
		
		.append("electricCharge", this.electricCharge)
		.append("weishengCharge", this.weishengCharge)
		.append("zhaomingCharge", this.zhaomingCharge)
		.append("paiwuCharge", this.paiwuCharge)
		.append("zhinajin", this.zhinajin)
		
		.append("readMeterDate", this.readMeterDate);
		return sb.toString();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((readoutsElectric == null) ? 0 : readoutsElectric.hashCode());
		result = prime * result	+ ((provReadoutsElectric == null) ? 0 : provReadoutsElectric.hashCode());
		result = prime * result	+ ((newReadoutsElectric == null) ? 0 : newReadoutsElectric.hashCode());		
		result = prime * result	+ ((readMeterDate == null) ? 0 : readMeterDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HpsElectricChaobiao other = (HpsElectricChaobiao) obj;
		if (readoutsElectric == null) {
			if (other.readoutsElectric != null)
				return false;
		} else if (!readoutsElectric.equals(other.readoutsElectric))
			return false;
		if (provReadoutsElectric == null) {
			if (other.provReadoutsElectric != null)
				return false;
		} else if (!provReadoutsElectric.equals(other.provReadoutsElectric))
			return false;
		if (newReadoutsElectric == null) {
			if (other.newReadoutsElectric != null)
				return false;
		} else if (!newReadoutsElectric.equals(other.newReadoutsElectric))
			return false;		
		if (readMeterDate == null) {
			if (other.readMeterDate != null)
				return false;
		} else if (!readMeterDate.equals(other.readMeterDate))
			return false;
		return true;
	}
}
