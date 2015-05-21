package org.appfuse.dao.hps.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsHeatingChargeDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.model.HeatingChargeRecordPaginationResult;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Repository
public class HpsHeatingChargeDaoHibernate extends HpsGenericDaoHibernate<HpsHeatingChargeRecord, Long>
		implements HpsHeatingChargeDao {

	public HpsHeatingChargeDaoHibernate() {
		super(HpsHeatingChargeRecord.class);
	}

	@Override
	public List<HpsHeatingPaymentDate> getPaymentDates(String baseCode) {
		String hql = "from HpsHeatingPaymentDate where base.code = :baseCode order by payStartDate desc";
		Query query = createQuery(hql);
		query.setString("baseCode", baseCode);
		@SuppressWarnings("unchecked")
		List<HpsHeatingPaymentDate> payments = query.list();
		return payments;
	}
	
	@Override
	public List<HpsHeatingPaymentDate> getPaymentDates() {
		String hql = "from HpsHeatingPaymentDate";
		Query query = createQuery(hql);
		@SuppressWarnings("unchecked")
		List<HpsHeatingPaymentDate> payments = query.list();
		return payments;
	}
	
	@Override
	public HeatingChargeRecordPaginationResult getChargeRecords(
			HeatingChargeQueryParam queryParam) {
		String sql = null;
		Query query = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (queryParam == null || queryParam.isEmpty()) {
			sql = "from HpsHeatingChargeRecord";
		} else {
			sql = "from HpsHeatingChargeRecord where 1 = 1 ";
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
			StringBuilder sbOr = new StringBuilder();
			boolean diverted = queryParam.isDiverted();
			if (diverted) {
//				sql += " and diverted = :diverted";
				sbOr.append("diverted = :diverted or ");
				params.put("diverted", diverted);
			}
			boolean stopped = queryParam.isStopped();
			if (stopped) {
//				sql += " and stopped = :stopped";
				sbOr.append("stopped = :stopped or ");
				params.put("stopped", stopped);
			}
			boolean livingSohard = queryParam.isLivingSohard();
			if (livingSohard) {
//				sql += " and livingSohard = :livingSohard";
				sbOr.append("livingSohard = :livingSohard or ");
				params.put("livingSohard", livingSohard);
			}
			if (sbOr.length() > 0) {
				sbOr.insert(0, " and (");
				sbOr.append("1 = 2) ");
				sql += sbOr;
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
		Query queryCnt = createQuery("select count (id), sum(case when chargeState = 2 then 0.0 else normalHeatingCharge end),"
		        + " sum(case when (chargeState = 2 or chargeState = 1) then 0.0 else actualSumCharge end) " + sql);
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(queryParam.getOffset());
		query.setMaxResults(queryParam.getRows());
		HeatingChargeRecordPaginationResult result = new HeatingChargeRecordPaginationResult();
		result.setRows(query.list());
		Object[] tongjiResult = (Object[]) queryCnt.uniqueResult();
		result.setTotal(((Long) tongjiResult[0]).intValue());
		result.setYingshouHeatingCharge((Double) tongjiResult[1]);
		result.setYishouHeatingCharge((Double) tongjiResult[2]);
		return result;
	}

	@Override
	public List<HpsHeatingUnit> getHeatingUnit(HpsHeatingPaymentDate paymentDate) {
		Criteria c = createCriteria(HpsHeatingUnit.class);
		c.add(Restrictions.eq("paymentDate.id", paymentDate.getId()));
		return c.list();
	}

	@Override
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenXingzhiPreferential(
			HpsHeatingPaymentDate paymentDate) {
		Criteria c = createCriteria(HpsHeatingShenfenXingzhiPreferential.class);
		c.add(Restrictions.eq("paymentDate.id", paymentDate.getId()));
		return c.list();
	}

	@Override
	public List<HpsHeatingPaymentDatePreferential> getPaymentDatePreferential(
			HpsHeatingPaymentDate paymentDate) {
		Criteria c = createCriteria(HpsHeatingPaymentDatePreferential.class);
		c.add(Restrictions.eq("paymentDate.id", paymentDate.getId()));
		return c.list();
	}

	@Override
	public HpsHeatingPaymentDate getPaymentDate(Long paymentDateId) {
		return get(HpsHeatingPaymentDate.class, paymentDateId);
	}
	
	@Override
	public List<HpsHeatingChargeRecord> getDivertedChargeRecords(Long recordId) {
		String sql = "from HpsHeatingChargeRecord where divertToRecordId = :recordId "
				+ "and diverted = :diverted and chargeState = :chargeState";
		Query q = createQuery(sql);
		q.setLong("recordId", recordId);
		// 其实只要chargeState和recordId匹配的缴费记录，一定是已结转的状态
		// 此处为了保险起见，又加了下面这个条件
		q.setBoolean("diverted", true);
		// 已结转的都是已缴费的
		q.setInteger("chargeState", ChargeStateEnum.CHARGED.ordinal());
		return q.list();
	}
	
	@Override
	public List<HpsHeatingChargeRecord> getMustDivertChargeRecords(HpsHeatingChargeRecord record) {
		String sql = "from HpsHeatingChargeRecord where paymentDate.payStartDate < :payStartDate "
				+ "and house.id = :houseId and houseOwner.id = :ownerId and chargeState = :chargeState";
		Query q = createQuery(sql);
		q.setDate("payStartDate", record.getPaymentDate().getPayStartDate());
		q.setLong("houseId", record.getHouse().getId());
		q.setLong("ownerId", record.getHouseOwner().getId());
		q.setInteger("chargeState", ChargeStateEnum.UNCHARGED.ordinal());
		return q.list();
	}
	
	@Override
	public List<HpsHeatingChargeRecord> getChargeRecords(Long paymentDateId, Long houseId) {
		String sql = "from HpsHeatingChargeRecord where house.id = :houseId "
				+ "and paymentDate.id = :paymentDateId";
		Query q = createQuery(sql);
		q.setLong("houseId", houseId);
		q.setLong("paymentDateId", paymentDateId);
		return q.list();
	}

}
