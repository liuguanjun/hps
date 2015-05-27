package com.my.hps.webapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.enums.BaseEnumJsonSerializer;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 2015房屋供暖维修费缴纳记录
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_maintain_charge_record_2015")
public class HpsHeatingMaintainChargeRecord2015 extends HpsBaseObject {

	private static final long serialVersionUID = 1484915212722947802L;

	/**
	 * 取暖费缴纳日期
	 */
	private HpsHeatingMaintainPaymentDate2015 paymentDate;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner houseOwner;
	
	/**
	 * 房屋
	 */
	private HpsHouse house;
	
	/**
	 * 缴纳日期
	 */
	private Date chargeDate;
	
	/**
	 * 缴费状态
	 */
	private ChargeStateEnum chargeState;
	
	/**
	 * 应该收取金额
	 */
	private Double mustCharge;
	
	/**
	 * 实际收取金额
	 */
	private Double actualCharge;
	
	/**
	 * 备注
	 */
	private String remarks;
	
	/**
	 * 操作员
	 */
	private HpsUser operUser;
	
	/**
	 * 被取消的原因
	 */
	private String cancelledCause;
	
	/**
	 * 工资号
	 */
	private String wageNum;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsHeatingMaintainPaymentDate2015 getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate) {
		this.paymentDate = paymentDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "house_owner_id")
	public HpsHouseOwner getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(HpsHouseOwner houseOwner) {
		this.houseOwner = houseOwner;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "house_id")
	public HpsHouse getHouse() {
		return house;
	}

	public void setHouse(HpsHouse house) {
		this.house = house;
	}

	@Column(name = "wage_num", length = 200)
	public String getWageNum() {
		return wageNum;
	}

	public void setWageNum(String wageNum) {
		this.wageNum = wageNum;
	}

	@Column(nullable = true, name = "charge_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	@Column(name = "charge_state", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@JsonSerialize(using = BaseEnumJsonSerializer.class)
	public ChargeStateEnum getChargeState() {
		return chargeState;
	}

	public void setChargeState(ChargeStateEnum chargeState) {
		this.chargeState = chargeState;
	}

	@Column(length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "oper_user_id")
	public HpsUser getOperUser() {
		return operUser;
	}

	public void setOperUser(HpsUser operUser) {
		this.operUser = operUser;
	}
	
	@Column(name = "cancelled_cause", nullable = true, length = 500)
	public String getCancelledCause() {
		return cancelledCause;
	}

	public void setCancelledCause(String cancelledCause) {
		this.cancelledCause = cancelledCause;
	}

	@Column(name = "must_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustCharge() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return mustCharge;
		} else {
			Double result = paymentDate.getUnit() * house.getWarmArea();
			BigDecimal resultBigDecimal = new BigDecimal(result);
			resultBigDecimal = resultBigDecimal.setScale(2, RoundingMode.HALF_UP);
			return resultBigDecimal.doubleValue();
		}
	}

	public void setMustCharge(Double mustCharge) {
		this.mustCharge = mustCharge;
	}

	@Column(nullable = false, name = "actual_charge")
	@JsonSerialize(using = MoneySerializer.class)
	public Double getActualCharge() {
		return actualCharge;
	}

	public void setActualCharge(Double actualCharge) {
		this.actualCharge = actualCharge;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualCharge == null) ? 0 : actualCharge.hashCode());
		result = prime * result
				+ ((chargeDate == null) ? 0 : chargeDate.hashCode());
		result = prime * result
				+ ((chargeState == null) ? 0 : chargeState.hashCode());
		result = prime * result + ((house == null) ? 0 : house.hashCode());
		result = prime * result
				+ ((houseOwner == null) ? 0 : houseOwner.hashCode());
		result = prime * result
				+ ((operUser == null) ? 0 : operUser.hashCode());
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
		HpsHeatingMaintainChargeRecord2015 other = (HpsHeatingMaintainChargeRecord2015) obj;
		if (actualCharge == null) {
			if (other.actualCharge != null)
				return false;
		} else if (!actualCharge.equals(other.actualCharge))
			return false;
		if (chargeDate == null) {
			if (other.chargeDate != null)
				return false;
		} else if (!chargeDate.equals(other.chargeDate))
			return false;
		if (chargeState != other.chargeState)
			return false;
		if (house == null) {
			if (other.house != null)
				return false;
		} else if (!house.equals(other.house))
			return false;
		if (houseOwner == null) {
			if (other.houseOwner != null)
				return false;
		} else if (!houseOwner.equals(other.houseOwner))
			return false;
		if (operUser == null) {
			if (other.operUser != null)
				return false;
		} else if (!operUser.equals(other.operUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("paymentDate", this.paymentDate.getBase().getName())
			.append("chargeDate", this.chargeDate)
	        .append("actualCharge", this.actualCharge)
	        .append("operUser", this.operUser == null ? "" : this.operUser.getUserName())
			.append("chargeState", this.chargeState.getName());
		return sb.toString();
	}
	
}
