package com.my.hps.webapp.controller.queryparam;

public class ElectricChargeQueryParam extends HouseQueryParam {
	
	// 欠费，未欠费
	private String electricChargeState;
	
	public String getElectricChargeState() {
		return electricChargeState;
	}
	public void setElectricChargeState(String electricChargeState) {
		this.electricChargeState = electricChargeState;
	}

}
