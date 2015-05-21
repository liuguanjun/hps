package com.my.hps.webapp.controller.queryparam;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

public class WeixiufeiTongjiQueryParam  extends PaginationQueryParam {
	private Long paymentDateId;
	private Long operUserId;
	private Date starDate;
	private Date endDate;
	private Long baseId;
	
	public Long getOperUserId() {
		return operUserId;
	}
	public void setOperUserId(Long operUserId) {
		this.operUserId = operUserId;
	}
	public Date getStarDate() {
		return starDate;
	}
	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Long getBaseId() {
		return baseId;
	}
	public void setBaseId(Long baseId) {
		this.baseId = baseId;
	}
	public Long getPaymentDateId() {
		return paymentDateId;
	}
	public void setPaymentDateId(Long paymentDateId) {
		this.paymentDateId = paymentDateId;
	}
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("paymentDateId", this.paymentDateId)
			.append("oper_user_id", this.operUserId)
			.append("baseId", this.baseId)
	        .append("starDate", this.starDate)
	        .append("endDate", this.endDate);
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(operUserId.toString()) 
				&& StringUtils.isEmpty(paymentDateId.toString())
				&& StringUtils.isEmpty(baseId.toString())
				&& StringUtils.isEmpty(starDate.toString())
				&& StringUtils.isEmpty(endDate.toString());
	}
}
