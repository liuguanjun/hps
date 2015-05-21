package com.my.hps.webapp.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 电费收费记录
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_electric_charge_record")
public class HpsElectricChargeRecord extends HpsBaseObject implements Comparable<HpsElectricChargeRecord>{
	
	private static final long serialVersionUID = 4430027222509816561L;

	/**
	 * 本次收费对应的抄表记录的集合
	 */
	private Set<HpsElectricChaobiao> chaobiaoSet;
	
	/**
	 * 缴费时间
	 */
	private Date chargeDate;
	
	/**
	 * 上次缴费的剩余金额，虽然可以从上一次的缴费记录中获得的这个值
	 * 但是，为了方便针对上次缴费的余额做了冗余字段
	 */
	private Double previousSurplus;
	
	/**
	 * 本次实际缴纳的金额
	 */
	private Double actualCharge;
	
	/**
	 * 冗余字段：本次缴费的剩余金额（actualCharge + previousSurplus - mustCharge）
	 */
	private Double currentSurplus;
	
	/**
	 * 冗余字段：本次应该缴纳的金额（可以通过遍历chaobiaoSet进行累加得到此字段的值）
	 * 电费 ＋ 卫生费 ＋ 照明费 ＋ 排污费
	 */
	private Double mustCharge;
	
	/**
	 * 是否是被取消
	 */
	private Boolean cancelled;
	
	/**
	 * 被取消的原因
	 */
	private String cancelledCause;
	
	/**
	 * 缴费记录所对应的房屋
	 */
	private HpsHouse house;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner houseOwner;
	
	/**
	 * 收费员
	 */
	private HpsUser chargeUser;
	
	/**
	 * 收滞纳金
	 */
	private boolean zhinajinOn = false;
	
	/**
	 * 缴费记录对应的基地，不保存到数据库中
	 */
	private HpsBase base;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="chargeRecord")
	@OrderBy("readMeterDate ASC")
	public Set<HpsElectricChaobiao> getChaobiaoSet() {
		return chaobiaoSet;
	}

	public void setChaobiaoSet(Set<HpsElectricChaobiao> chaobiaoSet) {
		this.chaobiaoSet = chaobiaoSet;
	}

	@Column(name = "charge_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	@Column(name = "previous_surplus", nullable = false)
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getPreviousSurplus() {
		return previousSurplus;
	}

	public void setPreviousSurplus(Double previousSurplus) {
		this.previousSurplus = previousSurplus;
	}

	@Column(name = "actual_charge", nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getActualCharge() {
		return actualCharge;
	}
	
	@Column(nullable = false, name = "zhinajin_on")
	public boolean isZhinajinOn() {
		return zhinajinOn;
	}

	public void setZhinajinOn(boolean zhinajinOn) {
		this.zhinajinOn = zhinajinOn;
	}

	public void setActualCharge(Double actualCharge) {
		this.actualCharge = actualCharge;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "house_owner_id")
	public HpsHouseOwner getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(HpsHouseOwner houseOwner) {
		this.houseOwner = houseOwner;
	}

	@Column(name = "current_surplus", nullable = false)
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getCurrentSurplus() {
		return currentSurplus;
	}

	public void setCurrentSurplus(Double currentSurplus) {
		this.currentSurplus = currentSurplus;
	}

	@Column(name = "must_charge", nullable = false)
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getMustCharge() {
		return mustCharge;
	}

	public void setMustCharge(Double mustCharge) {
		this.mustCharge = mustCharge;
	}

	@Column(name = "cancelled", nullable = false)
	public Boolean getCancelled() {
		return cancelled;
	}

	@Column(name = "cancelled_cause", nullable = true, length = 500)
	public String getCancelledCause() {
		return cancelledCause;
	}

	public void setCancelledCause(String cancelledCause) {
		this.cancelledCause = cancelledCause;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "house_id")
	public HpsHouse getHouse() {
		return house;
	}

	public void setHouse(HpsHouse house) {
		this.house = house;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "oper_user_id")
	public HpsUser getChargeUser() {
		return chargeUser;
	}

	public void setChargeUser(HpsUser chargeUser) {
		this.chargeUser = chargeUser;
	}
	
	@Transient
	public HpsBase getBase() {
		return base;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@Override
	public int compareTo(HpsElectricChargeRecord o) {
		if (this.id == o.id) {
			return 0;
		} else {
			// 收费日期大的排在前面（小）
			return o.chargeDate.compareTo(this.chargeDate);
		}
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("chargeDate", this.chargeDate)
			.append("mustCharge", this.mustCharge)
	        .append("actualCharge", this.actualCharge)
			.append("currentSurplus", this.currentSurplus);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualCharge == null) ? 0 : actualCharge.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((chargeDate == null) ? 0 : chargeDate.hashCode());
		result = prime * result
				+ ((chargeUser == null) ? 0 : chargeUser.hashCode());
		result = prime * result
				+ ((currentSurplus == null) ? 0 : currentSurplus.hashCode());
		result = prime * result
				+ ((mustCharge == null) ? 0 : mustCharge.hashCode());
		result = prime * result
				+ ((previousSurplus == null) ? 0 : previousSurplus.hashCode());
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
		HpsElectricChargeRecord other = (HpsElectricChargeRecord) obj;
		if (actualCharge == null) {
			if (other.actualCharge != null)
				return false;
		} else if (!actualCharge.equals(other.actualCharge))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (chargeDate == null) {
			if (other.chargeDate != null)
				return false;
		} else if (!chargeDate.equals(other.chargeDate))
			return false;
		if (chargeUser == null) {
			if (other.chargeUser != null)
				return false;
		} else if (!chargeUser.equals(other.chargeUser))
			return false;
		if (currentSurplus == null) {
			if (other.currentSurplus != null)
				return false;
		} else if (!currentSurplus.equals(other.currentSurplus))
			return false;
		if (mustCharge == null) {
			if (other.mustCharge != null)
				return false;
		} else if (!mustCharge.equals(other.mustCharge))
			return false;
		if (previousSurplus == null) {
			if (other.previousSurplus != null)
				return false;
		} else if (!previousSurplus.equals(other.previousSurplus))
			return false;
		return true;
	}

	

}
