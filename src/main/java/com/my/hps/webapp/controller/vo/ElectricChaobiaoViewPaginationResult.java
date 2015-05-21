package com.my.hps.webapp.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.hps.webapp.model.ElectricMoneySerializer;
import com.my.hps.webapp.model.PaginationResult;

public class ElectricChaobiaoViewPaginationResult extends PaginationResult<HpsElectricChaobiaoView> {

    private Long yingshouElectricCount;
    
    private Double yingshouElectricCharge;
    
    private Long yishouElectricCount;
    
    private Double yishouElectricCharge;

    @JsonSerialize(using = ElectricMoneySerializer.class)
    public Long getYingshouElectricCount() {
        return yingshouElectricCount == null ? 0 : yingshouElectricCount;
    }

    public void setYingshouElectricCount(Long yingshouElectricCount) {
        this.yingshouElectricCount = yingshouElectricCount;
    }

    @JsonSerialize(using = ElectricMoneySerializer.class)
    public Double getYingshouElectricCharge() {
        return yingshouElectricCharge == null ? 0 : yingshouElectricCharge;
    }

    public void setYingshouElectricCharge(Double yingshouElectricCharge) {
        this.yingshouElectricCharge = yingshouElectricCharge;
    }

    @JsonSerialize(using = ElectricMoneySerializer.class)
    public Long getYishouElectricCount() {
        return yishouElectricCount == null ? 0 : yishouElectricCount;
    }

    public void setYishouElectricCount(Long yishouElectricCount) {
        this.yishouElectricCount = yishouElectricCount;
    }

    @JsonSerialize(using = ElectricMoneySerializer.class)
    public Double getYishouElectricCharge() {
        return yishouElectricCharge == null ? 0 : yishouElectricCharge;
    }

    public void setYishouElectricCharge(Double yishouElectricCharge) {
        this.yishouElectricCharge = yishouElectricCharge;
    }
    
    
	
	
}
