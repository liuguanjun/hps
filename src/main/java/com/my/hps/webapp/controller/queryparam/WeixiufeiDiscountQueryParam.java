package com.my.hps.webapp.controller.queryparam;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

public class WeixiufeiDiscountQueryParam extends PaginationQueryParam {
	
	/**
	 * 维修费缴费年度ID
	 */
	private Long paymentDateId;

	public Long getPaymentDateId() {
		return paymentDateId;
	}

	public void setPaymentDateId(Long paymentDateId) {
		this.paymentDateId = paymentDateId;
	}
	
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("paymentDateId", this.paymentDateId);
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(paymentDateId.toString());
	}
}
