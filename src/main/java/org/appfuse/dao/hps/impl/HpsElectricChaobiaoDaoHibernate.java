package org.appfuse.dao.hps.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsElectricChaobiaoDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.ElectricChaobiaoQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoPaginationResult;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricPaymentDate;
import com.my.hps.webapp.model.HpsElectricUnit;

@Repository
public class HpsElectricChaobiaoDaoHibernate extends HpsGenericDaoHibernate<HpsElectricChaobiao, Long> implements HpsElectricChaobiaoDao {

	public HpsElectricChaobiaoDaoHibernate() {
		super(HpsElectricChaobiao.class);
	}

	@Override
	public List<HpsElectricChaobiao> updateElectricChaobiao(List<HpsElectricChaobiao> electricityChaobiaoList) {
		for (HpsElectricChaobiao electricityChaobiao : electricityChaobiaoList) {
			saveOrUpdate(electricityChaobiao);
			flush();
		}
		return electricityChaobiaoList;
	}
	
	@Override
	public HpsElectricPaymentDate getPaymentDate(Long baseId, String month) {
		String sql = "from HpsElectricPaymentDate where base.id = :baseId and date_format(month, '%Y-%m')"
				+ " = :chargeMonth";
		Query qry = createQuery(sql);
		qry.setString("chargeMonth", month);
		qry.setLong("baseId", baseId);
		return (HpsElectricPaymentDate) qry.uniqueResult();
		
	}

	@Override
	public HpsElectricPaymentDate getElectricPaymentDate(Long paymentDateId) {
		Criteria c = createCriteria(HpsElectricPaymentDate.class);
		c.add(Restrictions.eq("id", paymentDateId));
		return (HpsElectricPaymentDate) c.uniqueResult();
	}

	@Override
	public ElectricChaobiaoPaginationResult getChaobiaos(ElectricChaobiaoQueryParam queryParam) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "from HpsElectricChaobiao where 1 = 1 ";
		String baseCode = queryParam.getBase_code();
		String areaCode = queryParam.getArea_code();
		if (StringUtils.isNotEmpty(baseCode)) {
			// 基地有选择
			sql += "and house.louzuo.area.base.code = :baseCode ";
			params.put("baseCode", baseCode);
		}
		if (StringUtils.isNotEmpty(areaCode)) {
			// 区域有选择
			sql += "and house.louzuo.area.code = :areaCode ";
			params.put("areaCode", areaCode);
		}
		String louzuoCode = queryParam.getLouzuo_code();
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
		String ownerName = queryParam.getHouseOwnerName();
		if (StringUtils.isNotEmpty(ownerName)) {
			sql += " and houseOwner.name like :ownerName";
			params.put("ownerName", "%" + ownerName + "%");
		}
		String houseNo = queryParam.getHouse_no();
		if (StringUtils.isNotEmpty(houseNo)) {
			sql += " and house.no like :houseNo";
			params.put("houseNo", "%" + houseNo + "%");
		}
		long paymentDateId = queryParam.getPaymentDateId();
		if (paymentDateId > 0) {
			sql += " and paymentDate.id = :paymentDateId";
			params.put("paymentDateId", paymentDateId);
		}
		// 已抄表，未抄表
		StringBuilder sbOr = new StringBuilder();
		boolean recorded = queryParam.isRecorded();
		if (recorded) {
			sbOr.append("readoutsElectric is not null or ");
		}
		boolean noRecord = queryParam.isNoRecord();
		if (noRecord) {
			sbOr.append("readoutsElectric is null or ");
		}
		if (sbOr.length() > 0) {
			sbOr.insert(0, " and (");
			sbOr.append("1 = 2) ");
			sql += sbOr;
		}
		// 缴费，未缴费
		sbOr = new StringBuilder();
		boolean charged = queryParam.isCharged();
		if (charged) {
			sbOr.append("chargeRecord is not null or ");
		}
		boolean unCharged = queryParam.isUnCharged();
		if (unCharged) {
			sbOr.append("chargeRecord is null or ");
		}
		if (sbOr.length() > 0) {
			sbOr.insert(0, " and (");
			sbOr.append("1 = 2) ");
			sql += sbOr;
		}
		// 新老用户
		sbOr = new StringBuilder();
		boolean newElectricUser = queryParam.isNewElectricUser();
		if (newElectricUser) {
			sbOr.append("provReadoutsElectric is null or ");
		}
		boolean oldElectricUser = queryParam.isOldElectricUser();
		if (oldElectricUser) {
			sbOr.append("provReadoutsElectric is not null or ");
		}
		if (sbOr.length() > 0) {
			sbOr.insert(0, " and (");
			sbOr.append("1 = 2) ");
			sql += sbOr;
		}
		Query query = createQuery(sql);
		// 记录条数、应收电量、应收电费、已收电量、已收电费
		Query queryCnt = createQuery("select count (id), sum(electricCount), sum(case when electricCharge is null then 0.0 else electricCharge end), "
		        + " sum(case when chargeRecord.id is null then 0 else electricCount end), "
		        + " sum(case when chargeRecord.id is null then 0.0 else electricCharge end) " + sql);
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(queryParam.getOffset());
		query.setMaxResults(queryParam.getRows());
		List<HpsElectricChaobiao> chaobiaos = query.list();
		ElectricChaobiaoPaginationResult result = new ElectricChaobiaoPaginationResult();
		result.setRows(chaobiaos);
		Object[] tongjiResult = (Object[]) queryCnt.uniqueResult();
		Long cntLong = (Long) tongjiResult[0];
		result.setTotal(cntLong.intValue());
		result.setYingshouElectricCount((Long) tongjiResult[1]);
		result.setYingshouElectricCharge((Double) tongjiResult[2]);
		result.setYishouElectricCount((Long) tongjiResult[3]);
		result.setYishouElectricCharge((Double) tongjiResult[4]);
		return result;
	}

	@Override
	public List<HpsElectricPaymentDate> getPaymentDates(String baseCode) {
		String hql = "from HpsElectricPaymentDate where base.code = :baseCode order by month";
		Query query = createQuery(hql);
		query.setParameter("baseCode", baseCode);
		@SuppressWarnings("unchecked")
		List<HpsElectricPaymentDate> paymentDates = query.list();
		return paymentDates;
	}

	@Override
	public List<HpsElectricChaobiao> getChaobiaos(Long paymentDateId) {
		String hql = "from HpsElectricChaobiao where paymentDate.id = :paymentDateId";
		Query query = createQuery(hql);
		query.setParameter("paymentDateId", paymentDateId);
		@SuppressWarnings("unchecked")
		List<HpsElectricChaobiao> chaobiaos = query.list();
		return chaobiaos;
	}

	@Override
	public List<HpsElectricUnit> getElectricUnits(String baseCode) {
		String hql = "from HpsElectricUnit where base.code = :baseCode and history = :history";
		Query query = createQuery(hql);
		query.setParameter("baseCode", baseCode);
		query.setParameter("history", false);
		@SuppressWarnings("unchecked")
		List<HpsElectricUnit> units = query.list();
		return units;
	}

    @Override
    public List<HpsElectricChaobiao> getPreviousInputedChaobiaos(Long paymentdateId, List<Long> houseIds) {
        if (houseIds.size() == 0) {
            return Collections.emptyList();
        }
        HpsElectricPaymentDate paymentDate = getElectricPaymentDate(paymentdateId);
        String sql1 = "SELECT chaobiao1.id, chaobiao1.house_id,  paymentdate1.month "
                + "    FROM "
                + "     hps.hps_electric_chaobiao chaobiao1,"
                + "     hps.hps_electric_payment_date paymentdate1"
                + "   where chaobiao1.payment_date_id = paymentdate1.id"
                + "   and paymentdate1.base_id = :baseId"
                + "   and paymentdate1.id != :paymentdateId";
        String sql2 = "SELECT chaobiao.house_id, max(paymentdate.month)"
                + "        FROM "
                + "         hps.hps_electric_chaobiao chaobiao,"
                + "         hps.hps_electric_payment_date paymentdate"
                + "       where chaobiao.payment_date_id = paymentdate.id"
                + "        and paymentdate.id != :paymentdateId"
                + "        and paymentdate.base_id = :baseId "
                + "        and chaobiao.readoutsElectric is not null"
                + "    group by chaobiao.house_id";
        SQLQuery sql1Query = createSQLQuery(sql1);
        sql1Query.setParameter("paymentdateId", paymentdateId);
        sql1Query.setParameter("baseId", paymentDate.getBase().getId());
        List<Object[]> allChaobiaoAttrArrayList = sql1Query.list();
        
        SQLQuery sql2Query = createSQLQuery(sql2);
        sql2Query.setParameter("paymentdateId", paymentdateId);
        sql2Query.setParameter("baseId", paymentDate.getBase().getId());
        List<Object[]> allChaobiaoMaxAttrArrayList = sql2Query.list();
        List<Long> chaobiaoIds = new ArrayList<Long>();
        for (Object[] chaobiaoAttrArray : allChaobiaoAttrArrayList) {
            Long houseId = ((Number) chaobiaoAttrArray[1]).longValue();
            Date month = (Date) chaobiaoAttrArray[2];
            for (Object[] chaobiaoMaxAttrArray : allChaobiaoMaxAttrArrayList) {
                Long houseIdMax = ((Number) chaobiaoMaxAttrArray[0]).longValue();
                Date monthMax = (Date) chaobiaoMaxAttrArray[1];
                if (houseId.equals(houseIdMax) && month.equals(monthMax) && houseIds.contains(houseId)) {
                    chaobiaoIds.add(((Number) chaobiaoAttrArray[0]).longValue());
                    break;
                }
            }
        }
        if (chaobiaoIds.size() == 0) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        List<HpsElectricChaobiao> resultChaobiaos = createCriteria(HpsElectricChaobiao.class)
                .add(Restrictions.in("id", chaobiaoIds))
                .list();
//        List<HpsElectricChaobiao> resultChaobiaos = new ArrayList<HpsElectricChaobiao>();
//        String hql = "from HpsElectricChaobiao chaobiao where chaobiao.id in (:chaobiaoIds)";
//        while (true) {
//            int endIndex = 100;
//            if (chaobiaoIds.size() < 100) {
//                endIndex = chaobiaoIds.size();
//            }
//            List<Long> shortChaobiaoIds = chaobiaoIds.subList(0, endIndex);
//            Query query = createQuery(hql);
//            query.setParameterList("chaobiaoIds", shortChaobiaoIds);
//            @SuppressWarnings("unchecked")
//            List<HpsElectricChaobiao> chaobiaos = query.list();
//            resultChaobiaos.addAll(chaobiaos);
//            chaobiaoIds.removeAll(shortChaobiaoIds);
//            if (chaobiaoIds.size() == 0) {
//                break;
//            }
//        }
        return resultChaobiaos;
    }
	
}
