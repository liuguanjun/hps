package com.my.hps.webapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 2015年单户供暖改造收费日期
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_heating_maintain_payment_date_2015")
public class HpsHeatingMaintainPaymentDate2015 extends HpsBaseObject {

    private static final long serialVersionUID = 4198072335711937159L;

    /**
     * 基地
     */
    private HpsBase base;
    
    /**
     * 标题
     */
    private String title;

    /**
     * 缴费开始日期
     */
    private Date payStartDate;

    /**
     * 缴费结束日期
     */
    private Date payEndDate;

    /**
     * 每平米单价
     */
    private Double unit;

    /**
     * 备注
     */
    private String remarks;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false, name = "base_id")
    public HpsBase getBase() {
        return base;
    }

    public void setBase(HpsBase base) {
        this.base = base;
    }

    @Column(nullable = true, name = "pay_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getPayStartDate() {
        return payStartDate;
    }

    public void setPayStartDate(Date payStartDate) {
        this.payStartDate = payStartDate;
    }

    @Column(nullable = true, name = "pay_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(Date payEndDate) {
        this.payEndDate = payEndDate;
    }
    
    @Column(nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 500)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(nullable = false)
    @JsonSerialize(using = MoneySerializer.class)
    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.id)
                .append("base", this.base.getName()).append("payStartDate", this.payStartDate)
                .append("payEndDate", this.payEndDate)
                .append("unit", this.unit);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((payEndDate == null) ? 0 : payEndDate.hashCode());
        result = prime * result + ((payStartDate == null) ? 0 : payStartDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HpsHeatingMaintainPaymentDate2015 other = (HpsHeatingMaintainPaymentDate2015) obj;
        if (base == null) {
            if (other.base != null)
                return false;
        } else if (!base.equals(other.base))
            return false;
        if (payEndDate == null) {
            if (other.payEndDate != null)
                return false;
        } else if (!payEndDate.equals(other.payEndDate))
            return false;
        if (payStartDate == null) {
            if (other.payStartDate != null)
                return false;
        } else if (!payStartDate.equals(other.payStartDate))
            return false;
        if (unit == null) {
            if (other.unit != null)
                return false;
        } else if (!unit.equals(other.unit))
            return false;
        return true;
    }

}
