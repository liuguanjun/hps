package com.my.hps.webapp.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.MoneySerializer;

public class HpsHeatingMaintainUserTongjiRowView {

	private HpsUser operUser;
	
	private Double mustCharge = 0d;
	
	private Double actualCharge = 0d;
	
	private int houseCount = 0;
	
	private String operName;
	
	public HpsUser getOperUser() {
		return operUser;
	}

	public void setOperUser(HpsUser operUser) {
		this.operUser = operUser;
	}
	
	@JsonSerialize(using = MoneySerializer.class)
	public Double getActualCharge() {
		return actualCharge;
	}

	public void setActualCharge(Double actualCharge) {
		this.actualCharge = actualCharge;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	@JsonSerialize(using = MoneySerializer.class)
    public Double getMustCharge() {
        return mustCharge;
    }

    public void setMustCharge(Double mustCharge) {
        this.mustCharge = mustCharge;
    }

    @JsonSerialize(using = MoneySerializer.class)
    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
    }
    
    

	
}
