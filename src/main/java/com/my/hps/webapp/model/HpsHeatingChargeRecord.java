package com.my.hps.webapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
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
import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.enums.BaseEnumJsonSerializer;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 取暖费缴纳记录
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_charge_record")
public class HpsHeatingChargeRecord extends HpsBaseObject {

	private static final long serialVersionUID = 1484915212722947802L;

	/**
	 * 取暖费缴纳日期
	 */
	private HpsHeatingPaymentDate paymentDate;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner houseOwner;
	
	/**
	 * 房屋
	 */
	private HpsHouse house;
	
	/**
	 * 缴纳日期优惠（冗余字段）
	 */
	private HpsHeatingPaymentDatePreferential paymentDatePreferential;
	
	/**
	 * 身份性质优惠（冗余字段）
	 */
	private HpsHeatingShenfenXingzhiPreferential shenfenXingzhiPreferential;
	
	/**
	 * 单价（冗余字段）
	 */
	private HpsHeatingUnit unit;
	
	/**
	 * 缴纳日期
	 */
	private Date chargeDate;
	
	/**
	 * 缴费状态
	 */
	private ChargeStateEnum chargeState;
	
	/**
	 * 标收取暖金额
	 */
	private Double normalHeatingCharge;
	
	/**
	 * 应该收取的取暖费金额
	 */
	private Double mustHeatingCharge;
	
	/**
	 * 应该缴纳的滞纳金
	 */
	private Double mustZhinajin;
	
	/**
	 * 优惠金额
	 */
	private Double preferential;
	
	/**
	 * 应该收取的合计
	 */
	private Double mustSumCharge;
	
	/**
	 * 实际收取的合计
	 */
	private Double actualSumCharge;
	
	/**
	 * 逾期天数
	 */
	private Integer expiredDays;
	
	/**
	 * 收滞纳金（大部分情况下都不会要求房主缴纳滞纳金）
	 */
	private boolean zhinajinOn = false;
	
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
	 * 是否停供
	 */
	private boolean stopped = false;
	
	/**
	 * 困难住户
	 */
	private boolean livingSohard = false;
	
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsHeatingPaymentDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsHeatingPaymentDate paymentDate) {
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "payment_date_preferential_id")
	public HpsHeatingPaymentDatePreferential getPaymentDatePreferential() {
		return paymentDatePreferential;
	}

	public void setPaymentDatePreferential(
			HpsHeatingPaymentDatePreferential paymentDatePreferential) {
		this.paymentDatePreferential = paymentDatePreferential;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "shenfen_xingzhi_preferential_id")
	public HpsHeatingShenfenXingzhiPreferential getShenfenXingzhiPreferential() {
		return shenfenXingzhiPreferential;
	}

	public void setShenfenXingzhiPreferential(
			HpsHeatingShenfenXingzhiPreferential shenfenXingzhiPreferential) {
		this.shenfenXingzhiPreferential = shenfenXingzhiPreferential;
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
	public HpsHeatingUnit getUnit() {
		return unit;
	}

	public void setUnit(HpsHeatingUnit unit) {
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

	@Column(name = "charge_state", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@JsonSerialize(using = BaseEnumJsonSerializer.class)
	public ChargeStateEnum getChargeState() {
		return chargeState;
	}

	public void setChargeState(ChargeStateEnum chargeState) {
		this.chargeState = chargeState;
	}

	@Column(nullable = false, name = "must_heating_charge")
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustHeatingCharge() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return mustHeatingCharge;
		} else {
			return getNormalHeatingCharge() - getPreferential();
		}
	}

	public void setMustHeatingCharge(Double mustHeatingCharge) {
		this.mustHeatingCharge = mustHeatingCharge;
	}

	@Column(nullable = false, name = "must_zhinajin")
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustZhinajin() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return mustZhinajin;
		} else {
			int expiredDays = getExpiredDays();
			if (expiredDays <= 0) {
				return 0d;
			} else {
				return expiredDays * paymentDate.getZhinajinRate() * getMustHeatingCharge();
			}
		}
	}

	public void setMustZhinajin(Double mustZhinajin) {
		this.mustZhinajin = mustZhinajin;
	}

	@Column(nullable = false, name = "expired_days")
	public Integer getExpiredDays() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return expiredDays;
		} else {
			Long currentTimeMillis = System.currentTimeMillis();
			Long endDateMillis = DateUtils.truncate(paymentDate.getPayEndDate(), Calendar.DATE).getTime();
			Long expiredTimeMillis = currentTimeMillis - endDateMillis;
			if (expiredTimeMillis < 0) {
				return 0;
			} else {
				return (int) (expiredTimeMillis / (1000 * 60 * 60 * 24));
			}
		}
	}

	public void setExpiredDays(Integer expiredDays) {
		this.expiredDays = expiredDays;
	}

	@Column(nullable = false, name = "zhinajin_on")
	public boolean isZhinajinOn() {
		return zhinajinOn;
	}

	public void setZhinajinOn(boolean zhinajinOn) {
		this.zhinajinOn = zhinajinOn;
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
	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	@Column(name = "must_sum_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getMustSumCharge() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return mustSumCharge;
		} else {
			Double result = 0d;
			result = getMustHeatingCharge() + (divertedCharge == null ? 0d : divertedCharge);
			BigDecimal divertedChargeBigDecimal = new BigDecimal(result);
			divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(2, RoundingMode.HALF_UP);
//			divertedChargeBigDecimal = divertedChargeBigDecimal.setScale(1, RoundingMode.HALF_UP);
			return divertedChargeBigDecimal.doubleValue();
		}
	}

	public void setMustSumCharge(Double mustSumCharge) {
		this.mustSumCharge = mustSumCharge;
	}

	@Column(name = "normal_heating_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getNormalHeatingCharge() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return normalHeatingCharge;
		} else {
			if (unit == null) {
				return 0d;
			} else {
				return unit.getUnit() * house.getWarmArea();
			}
		}
	}

	public void setNormalHeatingCharge(Double normalHeatingCharge) {
		this.normalHeatingCharge = normalHeatingCharge;
	}

	@Column(nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getPreferential() {
		if (chargeState == ChargeStateEnum.CHARGED) {
			return preferential;
		} else {
//			if (stopped) {
//				return (1 - paymentDate.getStopHeatingRate()) * getNormalHeatingCharge();
//			}
//			if (livingSohard) {
//				return (1 - paymentDate.getLivingSoHardRate()) * getNormalHeatingCharge();
//			}
			if (shenfenXingzhiPreferential != null) {
				return (1 - shenfenXingzhiPreferential.getPayRate()) * getNormalHeatingCharge();
			}
			if (paymentDatePreferential != null) {
				return paymentDatePreferential.getOffRate() * getNormalHeatingCharge();
			}
			return 0d;
		}
	}

	public void setPreferential(Double preferential) {
		this.preferential = preferential;
	}

	@Column(nullable = false, name = "actual_sum_charge")
	@JsonSerialize(using = MoneySerializer.class)
	public Double getActualSumCharge() {
		return actualSumCharge;
	}

	public void setActualSumCharge(Double actualSumCharge) {
		this.actualSumCharge = actualSumCharge;
	}
	
	@Column(name = "living_sohard")
	public boolean isLivingSohard() {
		return livingSohard;
	}

	public void setLivingSohard(boolean livingSohard) {
		this.livingSohard = livingSohard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (balanced ? 1231 : 1237);
		result = prime * result
				+ ((chargeDate == null) ? 0 : chargeDate.hashCode());
		result = prime * result
				+ ((chargeState == null) ? 0 : chargeState.hashCode());
		result = prime * result + ((house == null) ? 0 : house.hashCode());
		result = prime * result
				+ ((houseOwner == null) ? 0 : houseOwner.hashCode());
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
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
		HpsHeatingChargeRecord other = (HpsHeatingChargeRecord) obj;
		if (balanced != other.balanced)
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
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
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
	        .append("actualSumCharge", this.actualSumCharge)
	        .append("operUser", this.operUser == null ? "" : this.operUser.getUserName())
			.append("chargeState", this.chargeState.getName());
		return sb.toString();
	}
	
	@Transient
	public String getPreferentialDesc() {
		NumberFormat format = new DecimalFormat("#,##0");
		format.setRoundingMode(RoundingMode.HALF_UP); 
		if (shenfenXingzhiPreferential != null) {
			return shenfenXingzhiPreferential.getTitle();
		}
		if (paymentDatePreferential != null) {
			return paymentDatePreferential.getTitle();
		}
		if (chargeState == ChargeStateEnum.CHARGED) { // 只有结算之后，才能确定是否是困难住户以及停供
			if (livingSohard) {
				return "困难住户:收取" + format.format(paymentDate.getLivingSoHardRate() * 100) + "%";
			}
			if (stopped) {
				return "停供:收取" + format.format(paymentDate.getStopHeatingRate() * 100) + "%";
			}
		}
		return "";
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
