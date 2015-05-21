package com.my.hps.webapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 电费缴纳日期
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_electric_payment_date")
public class HpsElectricPaymentDate extends HpsBaseObject {
	
	private static final long serialVersionUID = 2767127371865658503L;

	/**
	 * 基地
	 */
	private HpsBase base;
	
	/**
	 * 电费缴纳月份
	 */
	private Date month;
	
	/**
	 * 缴纳开始日
	 */
	private Date startDate;
	
	/**
	 * 缴纳结束日
	 */
	private Date endDate;
	
	/**
	 * 当前缴纳日期所对应的缴费记录数据是否已经初始化，已经初始化之后，是不可以修改的
	 */
	private boolean chaobiaosInitialized;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "base_id")
	public HpsBase getBase() {
		return base;
	}
	
	@Column(nullable = false, name = "chaobiaos_initialized")
	public boolean isChaobiaosInitialized() {
		return chaobiaosInitialized;
	}

	public void setChaobiaosInitialized(boolean chaobiaosInitialized) {
		this.chaobiaosInitialized = chaobiaosInitialized;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(nullable = false, name = "start_date")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getStartDate() {
		return startDate;
	}
	
	@Transient
	public String getMonthFormatStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		return format.format(month);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(nullable = false, name = "end_date")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		HpsElectricPaymentDate other = (HpsElectricPaymentDate) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("base", this.base.getCode())
			.append("month", this.month)
			.append("startDate", this.startDate)
			.append("endDate", this.endDate);
		return sb.toString();
	}

}
