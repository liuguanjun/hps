package com.my.hps.webapp.job;

import org.appfuse.service.hps.HpsElectricChaobiaoManager;
import org.appfuse.service.hps.HpsElectricPaymentDateManager;
import org.appfuse.service.hps.HpsHeatingChargeManager;
import org.appfuse.service.hps.HpsHeatingMaintain2015ChargeManager;
import org.appfuse.service.hps.HpsMaintainChargeManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.hps.webapp.model.HpsElectricPaymentDate;

@Component
public class HpsChargeJob implements InitializingBean {
	
	private HpsHeatingChargeManager heatingMamager;
	private HpsMaintainChargeManager maitainManager;
	private HpsElectricChaobiaoManager chaobiaoManager;
	private HpsElectricPaymentDateManager elePaymentDateManager;
	private HpsHeatingMaintain2015ChargeManager heatingMaintainManager;
	
    @Scheduled(cron = "0 0 12 * * ?") // 每天12点
//	@Scheduled(cron = "0 0/5 * * * ?") // 五分钟一次，测试用
    public void initChargeRecords() {
		heatingMamager.initializeChargeRecords(); 
    	maitainManager.initializeChargeRecords();
    	elePaymentDateManager.initializePymentDates();
    	chaobiaoManager.initializeChaobiaoRecords();
    	heatingMaintainManager.initializeChargeRecords();
    }

    @Autowired
	public void setHeatingMamager(HpsHeatingChargeManager heatingMamager) {
		this.heatingMamager = heatingMamager;
	}
    
    @Autowired
	public void setMaintaiMamager(HpsMaintainChargeManager maitainManager) {
		this.maitainManager = maitainManager;
	}
    
    @Autowired
	public void setChaobiaoManager(HpsElectricChaobiaoManager chaobiaoManager) {
		this.chaobiaoManager = chaobiaoManager;
	}
    
    @Autowired
	public void setElePaymentDateManager(
			HpsElectricPaymentDateManager elePaymentDateManager) {
		this.elePaymentDateManager = elePaymentDateManager;
	}
    
    @Autowired
	public void setHeatingMaintainManager(HpsHeatingMaintain2015ChargeManager heatingMaintainManager) {
        this.heatingMaintainManager = heatingMaintainManager;
    }

    @Override
	public void afterPropertiesSet() throws Exception {
		initChargeRecords();
	}
    
}
