package com.my.hps.webapp.controller.queryparam;

public class ElectricTongjiQueryParam extends PaginationQueryParam {
	
	private String baseCode;
	
	private String areaCode;
	
//	private Long startPaymentDateId;
//	
//	private Long endPaymentDateId;
	
	private String startChargeTime;
	
	private String endChargeTime;
	
	private Long operUserId;
	
	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

//	public Long getStartPaymentDateId() {
//		return startPaymentDateId;
//	}
//
//	public void setStartPaymentDateId(Long startPaymentDateId) {
//		this.startPaymentDateId = startPaymentDateId;
//	}
//
//	public Long getEndPaymentDateId() {
//		return endPaymentDateId;
//	}
//
//	public void setEndPaymentDateId(Long endPaymentDateId) {
//		this.endPaymentDateId = endPaymentDateId;
//	}

	public String getStartChargeTime() {
		return startChargeTime;
	}

	public void setStartChargeTime(String startChargeTime) {
		this.startChargeTime = startChargeTime;
	}

	public String getEndChargeTime() {
		return endChargeTime;
	}

	public void setEndChargeTime(String endChargeTime) {
		this.endChargeTime = endChargeTime;
	}

	public Long getOperUserId() {
		return operUserId;
	}

	public void setOperUserId(Long operUserId) {
		this.operUserId = operUserId;
	}
	

}
