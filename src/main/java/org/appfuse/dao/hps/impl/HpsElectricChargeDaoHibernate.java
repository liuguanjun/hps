package org.appfuse.dao.hps.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsElectricChargeDao;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.my.hps.webapp.controller.queryparam.ElectricChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.PaginationQueryParam;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Repository
public class HpsElectricChargeDaoHibernate extends HpsGenericDaoHibernate<HpsElectricChargeRecord, Long> 
		implements HpsElectricChargeDao {

	public HpsElectricChargeDaoHibernate() {
		super(HpsElectricChargeRecord.class);
	}

	@Override
	public List<HpsElectricChaobiao> getChaobiaos(Collection<Long> houseIds) {
		if (CollectionUtils.isEmpty(houseIds)) {
			return Collections.emptyList();
		}
		@SuppressWarnings("unchecked")
		List<HpsElectricChaobiao> chaobiaos = createCriteria(HpsElectricChaobiao.class)
			.createAlias("house", "house")
			.add(Restrictions.in("house.id", houseIds))
			.addOrder(Order.asc("house.id"))
			.addOrder(Order.asc("readMeterDate"))
			.list();
		return chaobiaos;
	}
	
	@Override
//	@HpsDataAuthorization(type = TargetType.Query, attrName = "louzuo.area.base.id")
	// 此方法没有做数据权限，因为页面上传递过来的基地已经进行了过滤
	public PaginationResult<HpsHouse> getHouses(ElectricChargeQueryParam queryParam) {
		String sql = "SELECT"
				+ "    h.id, r.charge_date, if (r.current_surplus is null, 0, r.current_surplus) last_surplus,"
				+ "    sum(if (c.charge_record_id is null, c.electricCount, 0)) cnt, "
				+ "    if (r.current_surplus is null, 0, r.current_surplus) - sum(if (c.charge_record_id is null, c.electricCharge, 0)) cs "
				+ "FROM"
				+ "    hps.hps_house h"
				+ "        left outer join"
				+ "    hps.hps_house_owner owner ON h.owner_id = owner.id"
				+ "        left outer join"
				+ "    hps.hps_electric_charge_record r ON h.id = r.house_id"
				+ "        left outer join"
				+ "    hps.hps_electric_chaobiao c ON (h.id = c.house_id and c.electricCount >= 0)"
				+ "        left outer join"
				+ "    hps.hps_louzuo l ON h.louzuo_id = l.id"
				+ "        left outer join"
				+ "    hps.hps_area a ON l.area_id = a.id"
				+ "        left outer join"
				+ "    hps.hps_base b ON a.base_id = b.id"
				+ " where"
				+ "    (r.charge_date = (select"
				+ "        max(r2.charge_date)"
				+ "    from"
				+ "        hps.hps_electric_charge_record r2"
				+ "    where"
				+ "        r2.house_id = h.id) or r.charge_date is null) ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (queryParam != null && !queryParam.isEmpty()) {
			String baseCode = queryParam.getBaseCode();
			String areaCode = queryParam.getAreaCode();
			if (StringUtils.isNotEmpty(baseCode)) {
				// 基地有选择
				sql += "and b.code = :baseCode ";
				params.put("baseCode", baseCode);
			}
			if (StringUtils.isNotEmpty(areaCode)) {
				// 区域有选择
				sql += "and a.code = :areaCode ";
				params.put("areaCode", areaCode);
			}
			String louzuoCode = queryParam.getLouzuoCode();
			if (StringUtils.isNotEmpty(louzuoCode)) {
				sql += " and l.code = :louzuoCode";
				params.put("louzuoCode", louzuoCode);
			}
			String danyuan = queryParam.getDanyuan();
			if (StringUtils.isNotEmpty(danyuan)) {
				sql += " and h.danyuan = :danyuan";
				params.put("danyuan", danyuan);
			}
			String ceng = queryParam.getCeng();
			if (StringUtils.isNotEmpty(ceng)) {
				sql += " and h.ceng = :ceng";
				params.put("ceng", ceng);
			}
			String ownerName = queryParam.getOwnerName();
			if (StringUtils.isNotEmpty(ownerName)) {
				sql += " and owner.name like :ownerName";
				params.put("ownerName", "%" + ownerName + "%");
			}
			String shenfenXingzhiCode = queryParam.getShenfenXingzhiCode();
			if (StringUtils.isNotEmpty(shenfenXingzhiCode)) {
				sql += " and h.shenfenXingzhi.code = :shenfenXingzhiCode";
				params.put("shenfenXingzhiCode", shenfenXingzhiCode);
			}
			String gongshangNo = queryParam.getGongshangNo();
			if (StringUtils.isNotEmpty(gongshangNo)) {
				sql += " and h.gongshangNo like :gongshangNo";
				params.put("gongshangNo", "%" + gongshangNo + "%");
			}
			String yongfangXingzhiCode = queryParam.getYongfangXingzhiCode();
			if (StringUtils.isNotEmpty(yongfangXingzhiCode)) {
				sql += " and h.yongfangXingzhi.code = :yongfangXingzhiCode";
				params.put("yongfangXingzhiCode", yongfangXingzhiCode);
			}
			String ownerNo = queryParam.getOwnerNo();
			if (StringUtils.isNotEmpty(ownerNo)) {
				sql += " and owner.no like :ownerNo";
				params.put("ownerNo", "%" + ownerNo + "%");
			}
			String houseNo = queryParam.getHouseNo();
			if (StringUtils.isNotEmpty(houseNo)) {
				sql += " and h.no like :houseNo";
				params.put("houseNo", "%" + houseNo + "%");
			}
			String ownerIdCardNo = queryParam.getOwnerIdCardNo();
			if (StringUtils.isNotEmpty(ownerIdCardNo)) {
				sql += " and owner.idCardNo like :ownerIdCardNo";
				params.put("ownerIdCardNo", "%" + ownerIdCardNo + "%");
			}
			String remarks = queryParam.getRemarks();
			if (StringUtils.isNotEmpty(remarks)) {
				sql += " and owner.remarks like :remarks";
				params.put("remarks", "%" + remarks + "%");
			}
			sql += " group by h.id ";
			// 缴费状态
			String chargeStateStr = queryParam.getElectricChargeState();
			if (StringUtils.isNotEmpty(chargeStateStr)) {
				ChargeStateEnum chargeState = ChargeStateEnum.valueOf(chargeStateStr);
				if (chargeState == ChargeStateEnum.CHARGED) {
					sql = "select * from (" + sql + ") r";
					sql += " where r.cs >= 0 or r.cs is null";
				} else if (chargeState == ChargeStateEnum.UNCHARGED) {
					sql = "select * from (" + sql + ") r";
					sql += " where r.cs < 0";
				}
			}
		} else {
			sql += " group by h.id ";
		}
		SQLQuery query = createSQLQuery(sql);
		SQLQuery queryCnt = createSQLQuery("select count(id) from (" + sql + ") rcnt ");
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(queryParam.getOffset());
		query.setMaxResults(queryParam.getRows());
		List<Object[]> houseAttrArrayList = query.list();
		List<Long> houseIdList = new ArrayList<Long>();
		for (Object[] houseAttrArray : houseAttrArrayList) {
			Long houseId = ((Number) houseAttrArray[0]).longValue();
			houseIdList.add(houseId);
		}
		PaginationResult<HpsHouse> result = new PaginationResult<HpsHouse>();
		List<HpsHouse> houses = new ArrayList<HpsHouse>();
		if (houseIdList.size() > 0) {
			houses = createCriteria(HpsHouse.class)
					.add(Restrictions.in("id", houseIdList))
					.list();
		}
		result.setRows(houses);
		Long cntLong = ((Number) queryCnt.uniqueResult()).longValue();
		result.setTotal(cntLong.intValue());
		return result;
	}
	
	@Override
	public PaginationResult<HpsElectricChargeRecord> getChargeRecords(
			Long houseId, PaginationQueryParam param) {
		String hql = "from HpsElectricChargeRecord where house.id = :houseId order by chargeDate desc";
		Query query = createQuery(hql);
		Query queryCnt = createQuery("select count(id)" + hql);
		query.setParameter("houseId", houseId);
		queryCnt.setParameter("houseId", houseId);
		query.setFirstResult(param.getOffset());
		query.setMaxResults(param.getRows());
		List<HpsElectricChargeRecord> records = query.list();
		PaginationResult<HpsElectricChargeRecord> result = new PaginationResult<HpsElectricChargeRecord>();
		result.setRows(records);
		Long cntLong = (Long) queryCnt.uniqueResult();
		result.setTotal(cntLong.intValue());
		return result;
	}
	
	@Override
	public PaginationResult<HpsElectricChargeRecord> getChargeRecords(
			ElectricTongjiQueryParam param) {
		String hql = "from HpsElectricChargeRecord where house.louzuo.area.base.code = :baseCode and cancelled = :cancelled";
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		hqlParams.put("baseCode", param.getBaseCode());
		hqlParams.put("cancelled", false);
		String areaCode = param.getAreaCode();
		if (StringUtils.isNotEmpty(areaCode)) {
			hql += " and house.louzuo.area.code = :areaCode";
			hqlParams.put("areaCode", areaCode);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = param.getStartChargeTime();
		if (StringUtils.isNotEmpty(startTime)) {
			hql += " and chargeDate >= :startTime";
			try {
				hqlParams.put("startTime", format.parse(startTime));
			} catch (ParseException e) {
			}
		}
		String endTime = param.getEndChargeTime();
		if (StringUtils.isNotEmpty(startTime)) {
			hql += " and chargeDate <= :endTime";
			try {
				hqlParams.put("endTime", format.parse(endTime));
			} catch (ParseException e) {
			}
		}
		Long operUserIdParam = param.getOperUserId();
		if (operUserIdParam != null) {
			hql += " and chargeUser.id = :operUserId";
			hqlParams.put("operUserId", operUserIdParam);
		}
		Query query = createQuery(hql + " order by chargeDate desc");
		Query queryCnt = createQuery("select count (id) " + hql);
		for (Map.Entry<String, Object> paramEntry : hqlParams.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(param.getOffset());
		query.setMaxResults(param.getRows());
		@SuppressWarnings("unchecked")
		List<HpsElectricChargeRecord> records = query.list();
		PaginationResult<HpsElectricChargeRecord> result = new PaginationResult<HpsElectricChargeRecord>();
		result.setRows(records);
		Long cntLong = (Long) queryCnt.uniqueResult();
		result.setTotal(cntLong.intValue());
		return result;
	}

}
