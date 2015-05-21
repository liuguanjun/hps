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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.enums.BaseEnumJsonSerializer;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 房屋维修费缴纳记录
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_maintain_charge_record")
public class HpsMaintainChargeRecord extends HpsBaseObject {

	private static final long serialVersionUID = 1484915212722947802L;

	/**
	 * 取暖费缴纳日期
	 */
	private HpsMaintainPaymentDate paymentDate;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner houseOwner;
	
	/**
	 * 房屋
	 */
	private HpsHouse house;
	
	/**
	 * 单价（冗余字段）
	 */
	private HpsMaintainUnit unit;
	
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
	 * 收费金额是否平衡，有些户主可能实际收费金额小于应该缴纳的金额
	 */
	private boolean balanced = true;
	
	/**
	 * 备注
	 */
	private String remarks;
	
	/**
	 * 操作员
	 */
	private HpsUser operUser;
	
	/**
	 * 是否结转
	 */
	private boolean diverted = false;
	
	/**
	 * 是否免缴
	 */
	private boolean gratis = false;
	
	/**
	 * 结转到的消费记录的ID，比如：12年的取暖费结转到14年进行缴费，那么该字段的值，就是14年缴费记录的ID
	 */
	private Long divertToRecordId;
	
	/**
	 * 历年结转缴费金额(可以通过计算获得，冗余字段,且这个金额不包括滞纳金)
	 */
	private Double divertedCharge;
	
	/**
	 * 被取消的原因
	 */
	private String cancelledCause;
	
	/**
	 * 工资号
	 */
	private String wageNum;
	
	/**
	 * 缴费月数
	 */
	private Integer monthCount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsMaintainPaymentDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsMaintainPaymentDate paymentDate) {
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "unit_id")
	public HpsMaintainUnit getUnit() {
		return unit;
	}

	public void setUnit(HpsMaintainUnit unit) {
		this.unit = unit;
	}
	
	@Column(name = "diverted_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getDivertedCharge() {
		return divertedCharge;
	}

	public void setDivertedCharge(Double divertedCharge) {
		this.divertedCharge = divertedCharge;
	}

	@Column(nullable = true, name = "charge_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	@Column(nullable = false, name = "month_count")
	public Integer getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
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

	@Column(nullable = false, name = "balanced")
	public boolean isBalanced() {
		return balanced;
	}

	public void setBalanced(boolean balanced) {
		this.balanced = balanced;
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

	@Column
	public boolean isDiverted() {
		return diverted;
	}

	public void setDiverted(boolean diverted) {
		this.diverted = diverted;
	}

	@Column(name = "diverted_to_record_id")
	public Long getDivertToRecordId() {
		return divertToRecordId;
	}

	public void setDivertToRecordId(Long divertedRecordId) {
		this.divertToRecordId = divertedRecordId;
	}
	
	@Column
	public boolean isGratis() {
		return gratis;
	}

	public void setGratis(boolean gratis) {
		this.gratis = gratis;
	}
	
	@Column(name = "must_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustCharge() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return mustCharge;
		} else {
			if (unit == null) {
				return 0d;
			} else {
				Double result = unit.getUnit() * house.getRepairArea() * monthCount + divertedCharge;
				BigDecimal resultBigDecimal = new BigDecimal(result);
				resultBigDecimal = resultBigDecimal.setScale(2, RoundingMode.HALF_UP);
				return resultBigDecimal.doubleValue();
			}
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
		result = prime
				* result
				+ ((divertToRecordId == null) ? 0 : divertToRecordId.hashCode());
		result = prime * result + (diverted ? 1231 : 1237);
		result = prime * result + (gratis ? 1231 : 1237);
		result = prime * result + ((house == null) ? 0 : house.hashCode());
		result = prime * result
				+ ((houseOwner == null) ? 0 : houseOwner.hashCode());
		result = prime * result
				+ ((monthCount == null) ? 0 : monthCount.hashCode());
		result = prime * result
				+ ((operUser == null) ? 0 : operUser.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		HpsMaintainChargeRecord other = (HpsMaintainChargeRecord) obj;
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
		if (divertToRecordId == null) {
			if (other.divertToRecordId != null)
				return false;
		} else if (!divertToRecordId.equals(other.divertToRecordId))
			return false;
		if (diverted != other.diverted)
			return false;
		if (gratis != other.gratis)
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
		if (monthCount == null) {
			if (other.monthCount != null)
				return false;
		} else if (!monthCount.equals(other.monthCount))
			return false;
		if (operUser == null) {
			if (other.operUser != null)
				return false;
		} else if (!operUser.equals(other.operUser))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("paymentDate", this.paymentDate.getBase().getName() 
				+ ":" + this.paymentDate.getTitle())
			.append("chargeDate", this.chargeDate)
	        .append("diverted", this.diverted)
	        .append("actualCharge", this.actualCharge)
	        .append("operUser", this.operUser == null ? "" : this.operUser.getUserName())
			.append("chargeState", this.chargeState.getName());
		return sb.toString();
	}
	
	/**
	 * 这里说的结转缴费记录，即，之前的欠费的年份结转到当前年份的缴费记录，
	 * 此属性用于描述这些结转的缴费记录，并不持久化到数据库，只是供前台显示使用
	 */
	private String divertedMsg;

	@Transient
	public String getDivertedMsg() {
		return divertedMsg;
	}

	public void setDivertedMsg(String divertedMsg) {
		this.divertedMsg = divertedMsg;
	}
	
	
}
