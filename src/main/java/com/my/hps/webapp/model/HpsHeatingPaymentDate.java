package com.my.hps.webapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 取暖费缴纳日期定义
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_payment_date")
public class HpsHeatingPaymentDate extends HpsBaseObject {
	
	private static final long serialVersionUID = 1924386456406889213L;

	/**
	 * 基地
	 */
	private HpsBase base;
	
	/**
	 * 取暖费缴纳日期的标题，例如：2014~2015年度取暖费
	 */
	private String title;
	
	/**
	 * 缴费开始日期
	 */
	private Date payStartDate;
	
	/**
	 * 缴费结束日期
	 */
	private Date payEndDate;
	
	/**
	 * 滞纳金比例
	 */
	private Double zhinajinRate;
	
	/**
	 * 备注
	 */
	private String remarks;
	
	/**
	 * 当前缴纳日期所对应的缴费记录数据是否已经初始化，已经初始化之后，是不可以修改的
	 */
	private boolean chargeRecordsInitialized;
	
	/**
	 * 申请停供（热设施运行基础费）收取比例
	 */
	private Double stopHeatingRate;
	
	/**
	 * 困难住户收取比例
	 */
	private Double livingSoHardRate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "base_id")
	public HpsBase getBase() {
		return base;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@Column(nullable = false, length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(nullable = false, name = "pay_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getPayStartDate() {
		return payStartDate;
	}

	public void setPayStartDate(Date payStartDate) {
		this.payStartDate = payStartDate;
	}

	@Column(nullable = false, name = "pay_end_date")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(Date payEndDate) {
		this.payEndDate = payEndDate;
	}

	@Column(name = "zhinajin_rate")
	public Double getZhinajinRate() {
		return zhinajinRate;
	}

	public void setZhinajinRate(Double zhinajinRate) {
		this.zhinajinRate = zhinajinRate;
	}

	@Column(length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Column(name = "stop_heating_rate")
	public Double getStopHeatingRate() {
		return stopHeatingRate;
	}

	public void setStopHeatingRate(Double stopHeatingRate) {
		this.stopHeatingRate = stopHeatingRate;
	}

	@Column(name = "living_sohard_rate")
	public Double getLivingSoHardRate() {
		return livingSoHardRate;
	}

	public void setLivingSoHardRate(Double livingSoHardRate) {
		this.livingSoHardRate = livingSoHardRate;
	}
	
	@Column(name = "chargerecords_initialized", nullable = false)
	public boolean isChargeRecordsInitialized() {
		return chargeRecordsInitialized;
	}

	public void setChargeRecordsInitialized(boolean chargeRecordsInitialized) {
		this.chargeRecordsInitialized = chargeRecordsInitialized;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("base", this.base.getName())
			.append("title", this.title)
	        .append("payStartDate", this.payStartDate)
	        .append("payEndDate", this.payEndDate)
			.append("zhinajinRate", this.zhinajinRate);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((payEndDate == null) ? 0 : payEndDate.hashCode());
		result = prime * result
				+ ((payStartDate == null) ? 0 : payStartDate.hashCode());
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
		HpsHeatingPaymentDate other = (HpsHeatingPaymentDate) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (payEndDate == null) {
			if (other.payEndDate != null)
				return false;
		} else if (!payEndDate.equals(other.payEndDate))
			return false;
		if (payStartDate == null) {
			if (other.payStartDate != null)
				return false;
		} else if (!payStartDate.equals(other.payStartDate))
			return false;
		return true;
	}

}
