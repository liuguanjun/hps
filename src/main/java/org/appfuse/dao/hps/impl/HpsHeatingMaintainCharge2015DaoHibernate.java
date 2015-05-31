package org.appfuse.dao.hps.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsHeatingMaintainCharge2015Dao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.model.HeatingChargeRecordPaginationResult;
import com.my.hps.webapp.model.HeatingMaintainChargeRecordPaginationResult2015;
import com.my.hps.webapp.model.HpsHeatingMaintainChargeRecord2015;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Repository
public class HpsHeatingMaintainCharge2015DaoHibernate extends HpsGenericDaoHibernate<HpsHeatingMaintainChargeRecord2015, Long>
        implements HpsHeatingMaintainCharge2015Dao {

    public HpsHeatingMaintainCharge2015DaoHibernate() {
        super(HpsHeatingMaintainChargeRecord2015.class);
    }

    @Override
    public List<HpsHeatingMaintainPaymentDate2015> getPaymentDates() {
        String hql = "from HpsHeatingMaintainPaymentDate2015 order by base.code";
        Query query = createQuery(hql);
        return query.list();
    }

    @Override
    public HpsHeatingMaintainPaymentDate2015 getPaymentDate(Long paymentDateId) {
        Criteria c = createCriteria(HpsHeatingMaintainPaymentDate2015.class);
        c.add(Restrictions.eq("id", paymentDateId));
        return (HpsHeatingMaintainPaymentDate2015) c.uniqueResult();
    }

    @Override
    public HeatingMaintainChargeRecordPaginationResult2015 getChargeRecords(HeatingMaintainCharge2015QueryParam queryParam) {
        String sql = null;
        Query query = null;
        Map<String, Object> params = new HashMap<String, Object>();
        if (queryParam == null || queryParam.isEmpty()) {
            sql = "from HpsHeatingMaintainChargeRecord2015";
        } else {
            sql = "from HpsHeatingMaintainChargeRecord2015 where 1 = 1 ";
            Long paymentDateId = queryParam.getPaymentDateId();
            if (paymentDateId != null) {
                sql += "and paymentDate.id = :paymentDateId ";
                params.put("paymentDateId", paymentDateId);
            }
            String baseCode = queryParam.getBaseCode();
            if (StringUtils.isNotEmpty(baseCode)) {
                // 基地有选择
                sql += "and house.louzuo.area.base.code = :baseCode ";
                params.put("baseCode", baseCode);
            }
            String areaCode = queryParam.getAreaCode();
            if (StringUtils.isNotEmpty(areaCode)) {
                // 区域有选择
                sql += "and house.louzuo.area.code = :areaCode ";
                params.put("areaCode", areaCode);
            }
            String louzuoCode = queryParam.getLouzuoCode();
            if (StringUtils.isNotEmpty(louzuoCode)) {
                sql += " and house.louzuo.code = :louzuoCode";
                params.put("louzuoCode", louzuoCode);
            }
            String danyuan = queryParam.getDanyuan();
            if (StringUtils.isNotEmpty(danyuan)) {
                sql += " and house.danyuan = :danyuan";
                params.put("danyuan", danyuan);
            }
            String ceng = queryParam.getCeng();
            if (StringUtils.isNotEmpty(ceng)) {
                sql += " and house.ceng = :ceng";
                params.put("ceng", ceng);
            }
            String ownerName = queryParam.getOwnerName();
            if (StringUtils.isNotEmpty(ownerName)) {
                sql += " and houseOwner.name like :ownerName";
                params.put("ownerName", "%" + ownerName + "%");
            }
            String shenfenXingzhiCode = queryParam.getShenfenXingzhiCode();
            if (StringUtils.isNotEmpty(shenfenXingzhiCode)) {
                sql += " and house.shenfenXingzhi.code = :shenfenXingzhiCode";
                params.put("shenfenXingzhiCode", shenfenXingzhiCode);
            }
            String gongshangNo = queryParam.getGongshangNo();
            if (StringUtils.isNotEmpty(gongshangNo)) {
                sql += " and house.gongshangNo like :gongshangNo";
                params.put("gongshangNo", "%" + gongshangNo + "%");
            }
            String yongfangXingzhiCode = queryParam.getYongfangXingzhiCode();
            if (StringUtils.isNotEmpty(yongfangXingzhiCode)) {
                sql += " and house.yongfangXingzhi.code = :yongfangXingzhiCode";
                params.put("yongfangXingzhiCode", yongfangXingzhiCode);
            }
            String ownerNo = queryParam.getOwnerNo();
            if (StringUtils.isNotEmpty(ownerNo)) {
                sql += " and houseOwner.no like :ownerNo";
                params.put("ownerNo", "%" + ownerNo + "%");
            }
            String houseNo = queryParam.getHouseNo();
            if (StringUtils.isNotEmpty(houseNo)) {
                sql += " and house.no like :houseNo";
                params.put("houseNo", "%" + houseNo + "%");
            }
            String ownerIdCardNo = queryParam.getOwnerIdCardNo();
            if (StringUtils.isNotEmpty(ownerIdCardNo)) {
                sql += " and houseOwner.idCardNo like :ownerIdCardNo";
                params.put("ownerIdCardNo", "%" + ownerIdCardNo + "%");
            }
            String chargeStateStr = queryParam.getChargeState();
            if (StringUtils.isNotEmpty(chargeStateStr)) {
                ChargeStateEnum chargeState = ChargeStateEnum.valueOf(chargeStateStr);
                sql += " and chargeState = :chargeState";
                params.put("chargeState", chargeState);
            }
            String recordRemarks = queryParam.getRecordRemarks();
            if (StringUtils.isNotEmpty(recordRemarks)) {
                sql += " and remarks like :remarks";
                params.put("remarks", "%" + recordRemarks + "%");
            }
            String operName = queryParam.getOperName();
            if (StringUtils.isNotEmpty(operName)) {
                sql += " and operUser.user.firstName like :operName";
                params.put("operName", "%" + operName + "%");
            }
            String chargeDateStr = queryParam.getChargeDate();
            if (StringUtils.isNotEmpty(chargeDateStr)) {
                sql += " and date_format(chargeDate,'%Y-%m-%d') = :chargeDateStr";
                params.put("chargeDateStr", chargeDateStr);
            }
            String wageNum = queryParam.getWageNum();
            if (StringUtils.isNotEmpty(wageNum)) {
                sql += " and wageNum like :wageNum";
                params.put("wageNum", "%" + wageNum + "%");
            }
        }
        query = createQuery(sql);
        Query queryCnt = createQuery("select count (id), sum(case when chargeState = 2 then 0.0 else mustCharge end),"
                + " sum(case when (chargeState = 2 or chargeState = 1) then 0.0 else actualCharge end) " + sql);
        for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
            query.setParameter(paramEntry.getKey(), paramEntry.getValue());
            queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
        query.setFirstResult(queryParam.getOffset());
        query.setMaxResults(queryParam.getRows());
        HeatingMaintainChargeRecordPaginationResult2015 result = new HeatingMaintainChargeRecordPaginationResult2015();
        result.setRows(query.list());
        Object[] tongjiResult = (Object[]) queryCnt.uniqueResult();
        result.setTotal(((Long) tongjiResult[0]).intValue());
        result.setYingshouHeatingCharge((Double) tongjiResult[1]);
        result.setYishouHeatingCharge((Double) tongjiResult[2]);
        return result;
    }

    @Override
    public HpsHeatingMaintainPaymentDate2015 savePaymentDate(HpsHeatingMaintainPaymentDate2015 paymentDate) {
        super.saveOrUpdate(paymentDate);
        return paymentDate;
    }

    @Override
    public HpsHeatingMaintainChargeRecord2015 getChargeRecordByHouseId(Long houseId) {
        Criteria c = createCriteria(HpsHeatingMaintainChargeRecord2015.class);
        c.add(Restrictions.eq("house.id", houseId));
        return (HpsHeatingMaintainChargeRecord2015) c.uniqueResult();
    }


}
