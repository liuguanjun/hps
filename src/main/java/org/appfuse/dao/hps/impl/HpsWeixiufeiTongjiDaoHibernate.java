package org.appfuse.dao.hps.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsWeixiufeiTongjiDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.WeixiufeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsWeixiufeiTongjiView;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

@Repository
public class HpsWeixiufeiTongjiDaoHibernate  extends HpsGenericDaoHibernate<HpsMaintainChargeRecord, Long> implements HpsWeixiufeiTongjiDao {

	public HpsWeixiufeiTongjiDaoHibernate() {
		super(HpsMaintainChargeRecord.class);
	}

	@Override
	public List<HpsUser> getCaozuoyuan(String baseCode) {
		String hql = "from HpsUser where base.code=:baseCode";
		Query query = createQuery(hql);
		query.setString("baseCode", baseCode);
		@SuppressWarnings("unchecked")
		List<HpsUser> caozuoyuanList = query.list();
		return caozuoyuanList;
	}
	
	/**
	 * 取得收费户数,计划总额,实缴取暖费总额
	 */
	@Override
	public void getWeixiufeiTongjiHushu(HpsWeixiufeiTongjiView weixiufeiTongjiView, WeixiufeiTongjiQueryParam param) {
		// sum(标收取暖金额)
		String hql = "select count(distinct t.house.id) as id from HpsMaintainChargeRecord as t where t.chargeState=:chargeState ";
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);// TODO
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tongjiJihuaAndHushuList = query.list();
		Long countId = (Long)tongjiJihuaAndHushuList.get(0);
		weixiufeiTongjiView.setCountId(countId);
		return;
	}

	/**
	 * 取得收费户数,计划总额,实缴取暖费总额
	 */
	@Override
	public void getWeixiufeiTongjiJihuaAndHushu(HpsWeixiufeiTongjiView weixiufeiTongjiView, WeixiufeiTongjiQueryParam param) {
		// sum(标收取暖金额)
		String hql = "select count(t.id) as id,sum(t.mustCharge) as mustCharge,sum(t.actualCharge) as actualCharge from HpsMaintainChargeRecord t where t.chargeState=:chargeState ";
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);// TODO
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tongjiJihuaAndHushuList = query.list();
		Object[] maintainChargeRecord = (Object[])tongjiJihuaAndHushuList.get(0);    
		Double mustChargeSum = (Double)maintainChargeRecord[1];
		Double actualChargeSum = (Double)maintainChargeRecord[2];
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		weixiufeiTongjiView.setMustChargeSum(format.format(mustChargeSum==null?0d:mustChargeSum));
		weixiufeiTongjiView.setActualChargeSum(format.format(actualChargeSum==null?0d:actualChargeSum));

		return;
	}
	
	private String whereCondition(WeixiufeiTongjiQueryParam param) {
		StringBuffer sql = new StringBuffer();
		// 基地
		if (param.getBaseId() != null) {
			sql.append(" and t.house.louzuo.area.base.id=:baseId ");
		}
		// 收取年度
		if (param.getPaymentDateId() != null) {
			sql.append(" and t.paymentDate.id=:paymentDateId ");
		}
		// 操作员
		if (param.getOperUserId() != null) {
			sql.append(" and t.operUser.id=:operUserId ");
		}
		// 开始日期
		
		if (!"".equals(param.getStarDate()) && param.getStarDate() != null) {
			sql.append(" and t.chargeDate>=:startDate ");
			
		}
		// 结束日期
		if (!"".equals(param.getEndDate()) && param.getEndDate() != null) {
			sql.append(" and t.chargeDate<=:endDate ");
		}
		return sql.toString();
	}
	
	private void setWhereConditionParam(WeixiufeiTongjiQueryParam param, Query query) {
		// 基地
		if (param.getBaseId() != null) {
			query.setLong("baseId", param.getBaseId());
		}
		// 收取年度
		if (param.getPaymentDateId() != null) {
			query.setLong("paymentDateId", param.getPaymentDateId());
		}
		// 操作员
		if (param.getOperUserId() != null) {
			query.setLong("operUserId", param.getOperUserId());
		}
		// 开始日期
		if (!"".equals(param.getStarDate()) && param.getStarDate() != null) {
			query.setDate("startDate", param.getStarDate());
		}
		// 结束日期
		if (!"".equals(param.getEndDate()) && param.getEndDate() != null) {
			long endTime = param.getEndDate().getTime();
			long oneDay = 24 * 60 * 60 * 1000;
			endTime = endTime + oneDay - 1000;
			query.setTimestamp("endDate", new Date(endTime));
		}
	}
}