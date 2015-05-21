package com.my.hps.webapp.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 房屋维修费单价
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_maintain_unit")
public class HpsMaintainUnit extends HpsBaseObject {
	
	private static final long serialVersionUID = -2796637809490159622L;

	/**
	 * 缴纳日期
	 */
	private HpsMaintainPaymentDate paymentDate;
	
	/**
	 * 用房性质
	 */
	private HpsDictItem yongfangXingzhi;
	
	/**
	 * 每月每平米单价
	 */
	private Double unit;
	
	/**
	 * 备注
	 */
	private String remarks;
	

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsMaintainPaymentDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsMaintainPaymentDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "yongfang_xingzhi_id")
	public HpsDictItem getYongfangXingzhi() {
		return yongfangXingzhi;
	}

	public void setYongfangXingzhi(HpsDictItem yongfangXingzhi) {
		this.yongfangXingzhi = yongfangXingzhi;
	}

	@Column(nullable = false)
	@JsonSerialize(using = MoneySerializer.class)
	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	@Column(length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Transient
	public String getDesc() {
		NumberFormat numberFormat = new DecimalFormat("#,##0.00");
		return yongfangXingzhi.getName() + "：" + numberFormat.format(unit) + "元/平米";
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", this.id)
		.append("paymentDate", this.paymentDate.getBase().getName() 
				+ ":" + this.paymentDate.getTitle())
		.append("yongfangXingzhi", this.yongfangXingzhi.getName())
        .append("unit", this.unit)
        .append("remarks", this.remarks);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result
				+ ((yongfangXingzhi == null) ? 0 : yongfangXingzhi.hashCode());
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
		HpsMaintainUnit other = (HpsMaintainUnit) obj;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (yongfangXingzhi == null) {
			if (other.yongfangXingzhi != null)
				return false;
		} else if (!yongfangXingzhi.equals(other.yongfangXingzhi))
			return false;
		return true;
	}

}
