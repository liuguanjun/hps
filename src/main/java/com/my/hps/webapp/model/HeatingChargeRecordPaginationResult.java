package com.my.hps.webapp.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class HeatingChargeRecordPaginationResult extends PaginationResult<HpsHeatingChargeRecord> {

    private Double yingshouHeatingCharge;
    
    private Double yishouHeatingCharge;

    @JsonSerialize(using = MoneySerializer.class)
    public Double getYingshouHeatingCharge() {
        return yingshouHeatingCharge == null ? 0 : yingshouHeatingCharge;
    }

    public void setYingshouHeatingCharge(Double yingshouHeatingCharge) {
        this.yingshouHeatingCharge = yingshouHeatingCharge;
    }

    @JsonSerialize(using = MoneySerializer.class)
    public Double getYishouHeatingCharge() {
        return yishouHeatingCharge == null ? 0 : yishouHeatingCharge;
    }

    public void setYishouHeatingCharge(Double yishouHeatingCharge) {
        this.yishouHeatingCharge = yishouHeatingCharge;
    }
    
    
}
