package com.my.hps.webapp.model;

import java.text.DecimalFormat;

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

/**
 * 取暖费身份性质优惠
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_shenfen_xingzhi_preferential")
public class HpsHeatingShenfenXingzhiPreferential extends HpsBaseObject {
	
	private static final long serialVersionUID = 8416678563020277278L;

	/**
	 * 取暖费缴纳日期
	 */
	private HpsHeatingPaymentDate paymentDate;
	
	/**
	 * 身份性质
	 */
	private HpsDictItem shenfengXingzhi;
	
	/**
	 * 优惠的标题
	 */
	private String title;
	
	/**
	 * 收取的比例
	 */
	private Double payRate;
	
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
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "shenfen_xingzhi_id")
	public HpsDictItem getShenfengXingzhi() {
		return shenfengXingzhi;
	}

	public void setShenfengXingzhi(HpsDictItem shenfengXingzhi) {
		this.shenfengXingzhi = shenfengXingzhi;
	}

	@Column
	public String getTitle() {
		if (StringUtils.isNotEmpty(this.title)) {
			return this.title;
		}
		double payRate = this.payRate == null ? 0d: this.payRate;
		StringBuilder titleSB = new StringBuilder(this.shenfengXingzhi.getName());
		titleSB.append(":");
		titleSB.append("收取");
		DecimalFormat numberFormat = new DecimalFormat("#,###.#");
		titleSB.append(numberFormat.format(payRate * 100));
		titleSB.append("%取暖费");
		return titleSB.toString();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, name = "pay_rate")
	public Double getPayRate() {
		return payRate;
	}

	public void setPayRate(Double payRate) {
		this.payRate = payRate;
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
		result = prime * result + ((payRate == null) ? 0 : payRate.hashCode());
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result
				+ ((shenfengXingzhi == null) ? 0 : shenfengXingzhi.hashCode());
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
		HpsHeatingShenfenXingzhiPreferential other = (HpsHeatingShenfenXingzhiPreferential) obj;
		if (payRate == null) {
			if (other.payRate != null)
				return false;
		} else if (!payRate.equals(other.payRate))
			return false;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
			return false;
		if (shenfengXingzhi == null) {
			if (other.shenfengXingzhi != null)
				return false;
		} else if (!shenfengXingzhi.equals(other.shenfengXingzhi))
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
	        .append("shenfengXingzhi", this.shenfengXingzhi.getName())
	        .append("payRate", this.payRate)
			.append("remarks", this.remarks);
		return sb.toString();
	}
	

}
