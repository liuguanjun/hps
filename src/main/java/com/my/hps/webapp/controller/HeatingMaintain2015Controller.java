package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsHeatingMaintain2015ChargeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;

@Controller
@RequestMapping("/heatingmaintain2015/")
public class HeatingMaintain2015Controller extends BaseFormController {

    private HpsHeatingMaintain2015ChargeManager manager;

    @Autowired
    public void setWHeatingMaintainManager(HpsHeatingMaintain2015ChargeManager manager) {
        this.manager = manager;
    }

    @RequestMapping(method = RequestMethod.GET, value = "paymentdate/{recordId}")
    @ResponseBody
    public HpsHeatingMaintainPaymentDate2015 getPaymentDate(@PathVariable Long recordId) {
        HpsHeatingMaintainPaymentDate2015 paymentDate = manager.getPaymentDate(recordId);
        return paymentDate;
    }

    @RequestMapping(method = RequestMethod.GET, value = "paymentdates")
    @ResponseBody
    public List<HpsHeatingMaintainPaymentDate2015> getPaymentDates() {
        List<HpsHeatingMaintainPaymentDate2015> paymentDates = manager.getPaymentDates();
        return paymentDates;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "paymentdate")
    @ResponseBody
    public HpsHeatingMaintainPaymentDate2015 updatePaymentDate(
            @ModelAttribute HpsHeatingMaintainPaymentDate2015 paymentDate) {
        Long paymentDateId = paymentDate.getId();
        HpsHeatingMaintainPaymentDate2015 paymentDateInDB = manager.getPaymentDate(paymentDateId);
        paymentDateInDB.setTitle(paymentDate.getTitle());
        paymentDateInDB.setPayStartDate(paymentDate.getPayStartDate());
        paymentDateInDB.setPayEndDate(paymentDate.getPayEndDate());
        paymentDateInDB.setUnit(paymentDate.getUnit());
        manager.savePaymentDate(paymentDateInDB);
        return paymentDateInDB;
    }

    @RequestMapping(method = RequestMethod.GET, value = "chargerecords")
    @ResponseBody
    public HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(@ModelAttribute HeatingMaintainCharge2015QueryParam param) {
        HeatingMaintainChargeRecordPaginationResult2015 result = manager.getChargeRecords(param);
        return result;
    }

}
