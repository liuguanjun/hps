package com.my.hps.webapp.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MaintainChargeRecordPaginationResult extends PaginationResult<HpsMaintainChargeRecord> {

    private Double yingshouMaintainCharge;
    
    private Double yishouMaintainCharge;

    @JsonSerialize(using = MoneySerializer.class)
    public Double getYingshouMaintainCharge() {
        return yingshouMaintainCharge == null ? 0 : yingshouMaintainCharge;
    }

    public void setYingshouMaintainCharge(Double yingshouMaintainCharge) {
        this.yingshouMaintainCharge = yingshouMaintainCharge;
    }

    @JsonSerialize(using = MoneySerializer.class)
    public Double getYishouMaintainCharge() {
        return yishouMaintainCharge == null ? 0 : yishouMaintainCharge;
    }

    public void setYishouMaintainCharge(Double yishouMaintainCharge) {
        this.yishouMaintainCharge = yishouMaintainCharge;
    }
    
    
}
