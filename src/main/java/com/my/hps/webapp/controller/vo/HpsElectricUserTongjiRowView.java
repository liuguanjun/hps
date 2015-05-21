package com.my.hps.webapp.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.ElectricMoneySerializer;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.MoneySerializer;

public class HpsElectricUserTongjiRowView {

	private HpsUser operUser;
	
	private Double actualCharge = 0d;
	
	private Long juminMonthCount;
	
	private String operName;
	
	private Double electricCharge = 0d;
	
	private Double zhinajin = 0d;
	
	private Double weishengfei = 0d;
	
	private Double paiwufei = 0d;
	
	private Double zhaomingfei = 0d;

	private int receiptCnt = 0;
	
	private Long electricCount = 0l;
	
	public HpsUser getOperUser() {
		return operUser;
	}

	public void setOperUser(HpsUser operUser) {
		this.operUser = operUser;
	}
	
	@JsonSerialize(using = MoneySerializer.class)
	public Long getJuminMonthCount() {
		return juminMonthCount;
	}

	public void setJuminMonthCount(Long juminMonthCount) {
		this.juminMonthCount = juminMonthCount;
	}
	
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getElectricCharge() {
		return electricCharge;
	}

	public void setElectricCharge(Double electricCharge) {
		this.electricCharge = electricCharge;
	}

	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getZhinajin() {
		return zhinajin;
	}

	public void setZhinajin(Double zhinajin) {
		this.zhinajin = zhinajin;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getWeishengfei() {
		return weishengfei;
	}

	public void setWeishengfei(Double weishengfei) {
		this.weishengfei = weishengfei;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getPaiwufei() {
		return paiwufei;
	}

	public void setPaiwufei(Double paiwufei) {
		this.paiwufei = paiwufei;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public Double getZhaomingfei() {
		return zhaomingfei;
	}

	public void setZhaomingfei(Double zhaomingfei) {
		this.zhaomingfei = zhaomingfei;
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

	public void setReceiptCnt(int receiptCnt) {
		this.receiptCnt  = receiptCnt;
	}

	@JsonSerialize(using = MoneySerializer.class)
	public int getReceiptCnt() {
		return receiptCnt;
	}

	@JsonSerialize(using = MoneySerializer.class)
    public Long getElectricCount() {
        return electricCount;
    }

    public void setElectricCount(Long electricCount) {
        this.electricCount = electricCount;
    }
	
}
