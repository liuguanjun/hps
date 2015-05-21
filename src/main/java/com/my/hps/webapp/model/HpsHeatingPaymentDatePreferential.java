package com.my.hps.webapp.model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 取暖费缴纳日期优惠
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_payment_date_preferential")
public class HpsHeatingPaymentDatePreferential extends HpsBaseObject {
	
	private static final long serialVersionUID = 2230942994048497347L;

	/**
	 * 取暖费缴纳日期
	 */
	private HpsHeatingPaymentDate paymentDate;
	
	/**
	 * 优惠开始日期
	 */
	private Date startDate;
	
	/**
	 * 优惠结束日期
	 */
	private Date endDate;
	
	/**
	 * 优惠的标题
	 */
	private String title;
	
	/**
	 * 优惠的比例
	 */
	private Double offRate;
	
	/**
	 * 备注
	 */
	private String remarks;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "payment_date_id")
	public HpsHeatingPaymentDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(HpsHeatingPaymentDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(nullable = false, name = "start_date")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getStartDate() {
		return startDate;
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

	@Column
	public String getTitle() {
		if (StringUtils.isNotEmpty(this.title)) {
			return this.title;
		}
		DateFormat format = new SimpleDateFormat("MM/dd");
		String startStr = format.format(startDate);
		String endStr = format.format(endDate);
		String title = startStr + "~" + endStr + ":";
		title += "优惠";
		DecimalFormat numberFormat = new DecimalFormat("#,###.#");
		String percent = numberFormat.format(this.offRate * 100);
		title += percent;
		title += "%";
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, name = "off_rate")
	public Double getOffRate() {
		return offRate;
	}

	public void setOffRate(Double offRate) {
		this.offRate = offRate;
	}

	@Column(length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((offRate == null) ? 0 : offRate.hashCode());
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
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
		HpsHeatingPaymentDatePreferential other = (HpsHeatingPaymentDatePreferential) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (offRate == null) {
			if (other.offRate != null)
				return false;
		} else if (!offRate.equals(other.offRate))
			return false;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
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
			.append("paymentDate", this.paymentDate.getBase().getName() 
				+ ":" + this.paymentDate.getTitle())
			.append("title", this.title)
	        .append("startDate", this.startDate)
	        .append("endDate", this.endDate)
			.append("offRate", this.offRate);
		return sb.toString();
	}
	
}
