package com.my.hps.webapp.controller.queryparam;

public class HeatingMaintainCharge2015QueryParam extends HouseQueryParam {
	
	private Long paymentDateId;
	
	// 欠费，未欠费，已取消
	private String chargeState;
	
	/**
	 * 是否免缴
	 */
	private boolean gratis = false;
	
	/**
	 * 操作员
	 */
	private String operName;
	
	/**
	 * 缴费日期
	 */
	private String chargeDate;
	
	/**
	 * 工资号
	 */
	private String wageNum;

	public Long getPaymentDateId() {
		return paymentDateId;
	}

	public void setPaymentDateId(Long paymentDateId) {
		this.paymentDateId = paymentDateId;
	}
	
	public boolean isEmpty() {
		return super.isEmpty() && paymentDateId == null;
	}
	
	public String getChargeState() {
		return chargeState;
	}

	public void setChargeState(String chargeState) {
		this.chargeState = chargeState;
	}
	
	private String recordRemarks;

	public String getRecordRemarks() {
		return recordRemarks;
	}

	public void setRecordRemarks(String recordRemarks) {
		this.recordRemarks = recordRemarks;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}

	public boolean isGratis() {
		return gratis;
	}

	public void setGratis(boolean gratis) {
		this.gratis = gratis;
	}
	
	public String getWageNum() {
		return wageNum;
	}

	public void setWageNum(String wageNum) {
		this.wageNum = wageNum;
	}

}
