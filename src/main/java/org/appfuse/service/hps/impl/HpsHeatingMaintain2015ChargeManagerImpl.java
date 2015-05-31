package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsBaseDao;
import org.appfuse.dao.hps.HpsHeatingMaintainCharge2015Dao;
import org.appfuse.service.hps.HpsHeatingMaintain2015ChargeManager;
import org.appfuse.service.hps.HpsHouseManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.exception.PaymentDateNotExistsException;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsHeatingMaintainChargeRecord2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Service
public class HpsHeatingMaintain2015ChargeManagerImpl extends GenericManagerImpl<HpsHeatingMaintainChargeRecord2015, Long>
        implements HpsHeatingMaintain2015ChargeManager {
    
    private static final long BASE_LVSHUN_ID = 4l;
    
    private HpsHeatingMaintainCharge2015Dao concreteDao;
    private HpsBaseDao baseDao;
    private HpsHouseManager houseManager;
    
    @Autowired
    public void setPaymentDateDao(HpsHeatingMaintainCharge2015Dao dao) {
        this.dao = dao;
        this.concreteDao = dao;
    }
    
    @Autowired
    public void setBaseDao(HpsBaseDao baseDao) {
        this.baseDao = baseDao;
    }
    
    @Autowired
    public void setHouseManager(HpsHouseManager houseManager) {
        this.houseManager = houseManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HpsHeatingMaintainPaymentDate2015> getPaymentDates() {
        return concreteDao.getPaymentDates();
    }
    

    @Override
    @Transactional(readOnly = true)
    public HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(HeatingMaintainCharge2015QueryParam param) {
        HeatingMaintainChargeRecordPaginationResult2015 result = concreteDao.getChargeRecords(param);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public HpsHeatingMaintainChargeRecord2015 getChargeRecord(Long recordId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public void initializeChargeRecords() {
        List<HpsHeatingMaintainPaymentDate2015> paymentDates = getPaymentDates();
        if (paymentDates.size() > 0) {
            return;
        }
        List<HpsBase> bases = baseDao.getAll();
        for (HpsBase base : bases) {
            if (base.getId().equals(BASE_LVSHUN_ID)) {
                continue;
            }
            HpsHeatingMaintainPaymentDate2015 newPaymentDate = new HpsHeatingMaintainPaymentDate2015();
            newPaymentDate.setTitle("2015年单户供暖改造");
            newPaymentDate.setBase(base);
            newPaymentDate.setUnit(19d);
            concreteDao.savePaymentDate(newPaymentDate);
            HouseQueryParam houseQueryParam = new HouseQueryParam();
            houseQueryParam.setRows(Integer.MAX_VALUE);
            houseQueryParam.setBaseCode(base.getCode());
            List<HpsHouse> houses = houseManager.getHouses(houseQueryParam).getRows();
            for (HpsHouse house : houses) {
                HpsHeatingMaintainChargeRecord2015 newChargeRecord = new HpsHeatingMaintainChargeRecord2015();
                newChargeRecord.setPaymentDate(newPaymentDate);
                newChargeRecord.setHouse(house);
                newChargeRecord.setHouseOwner(house.getOwner());
                newChargeRecord.setWageNum(house.getOwner().getWageNum());
                newChargeRecord.setActualCharge(0d);
                newChargeRecord.setMustCharge(0d);
                newChargeRecord.setChargeState(ChargeStateEnum.UNCHARGED);
                concreteDao.save(newChargeRecord);
            }
        }
        
    }

    @Override
    @Transactional
    public HpsHeatingMaintainChargeRecord2015 charge(Long recordId, Double actualCharge, boolean gratis,
            String wageNum, String remarks) {
        return null;
    }

    @Override
    @Transactional
    public HpsHeatingMaintainChargeRecord2015 cancel(Long recordId, String remarks) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public HpsHeatingMaintainPaymentDate2015 getPaymentDate(Long paymentDateId) {
        return concreteDao.getPaymentDate(paymentDateId);
    }

    @Override
    @Transactional
    public HpsHeatingMaintainPaymentDate2015 savePaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate) {
        return concreteDao.savePaymentDate(paymentDate);
    }

    @Override
    @Transactional
    public void initializeChargeRecord(HpsHouse house) {
        Long houseId = house.getId();
        HpsHeatingMaintainChargeRecord2015 chargeRecord = concreteDao.getChargeRecordByHouseId(houseId);
        if (chargeRecord == null) {
            // 还没有当前房屋的缴费记录，生成一个新的
            HpsHeatingMaintainChargeRecord2015 newChargeRecord = new HpsHeatingMaintainChargeRecord2015();
            HpsHeatingMaintainPaymentDate2015 paymentDate = getPaymentDateOfHouse(house);
            newChargeRecord.setPaymentDate(paymentDate);
            newChargeRecord.setHouse(house);
            newChargeRecord.setHouseOwner(house.getOwner());
            newChargeRecord.setWageNum(house.getOwner().getWageNum());
            newChargeRecord.setActualCharge(0d);
            newChargeRecord.setMustCharge(0d);
            newChargeRecord.setChargeState(ChargeStateEnum.UNCHARGED);
            concreteDao.save(newChargeRecord);
        } else if (chargeRecord.getChargeState() == ChargeStateEnum.UNCHARGED) {
            chargeRecord.setHouseOwner(house.getOwner());
            chargeRecord.setWageNum(house.getOwner().getWageNum());
            concreteDao.save(chargeRecord);
        }
        
    }
    
    private HpsHeatingMaintainPaymentDate2015 getPaymentDateOfHouse(HpsHouse house) {
        String baseCode = house.getBaseCode();
        List<HpsHeatingMaintainPaymentDate2015> paymentDates = getPaymentDates();
        for (HpsHeatingMaintainPaymentDate2015 paymentDate : paymentDates) {
            if (paymentDate.getBase().getCode().equals(baseCode)) {
                return paymentDate;
            }
        }
        throw new PaymentDateNotExistsException();
    }


}
