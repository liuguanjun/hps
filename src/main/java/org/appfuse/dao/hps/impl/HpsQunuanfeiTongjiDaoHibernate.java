package org.appfuse.dao.hps.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsQunuanfeiTongjiDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.QunuanfeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsQunuanfeiTongjiView;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.ChargeStateEnum;
@Repository
public class HpsQunuanfeiTongjiDaoHibernate extends HpsGenericDaoHibernate<HpsHeatingChargeRecord, Long> implements HpsQunuanfeiTongjiDao {
	private Double g_jianmianAllCharge = 0d;
	public HpsQunuanfeiTongjiDaoHibernate() {
		super(HpsHeatingChargeRecord.class);
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
	 * 取得收费户数
	 */
	@Override
	public void getQunuanfeiTongjiHushu(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// 取得收费户数
		String hql = "select count(distinct t.house.id) as id from HpsHeatingChargeRecord t where t.chargeState=:chargeState ";
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);// TODO
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tongjiJihuaAndHushuList = query.list();
		Long countId=(Long)tongjiJihuaAndHushuList.get(0);
		qunuanfeiTongjiView.setCountId(countId);
		return;
	}

	/**
	 * 取得收费户数,计划总额,实缴取暖费总额
	 */
	@Override
	public void getQunuanfeiTongjiJihuaAndHushu(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(标收取暖金额)
		String hql = "select count(t.id) as id,sum(t.normalHeatingCharge) as normalHeatingCharge,sum(t.actualSumCharge) as actualSumCharge from HpsHeatingChargeRecord t where t.chargeState=:chargeState ";
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);// TODO
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tongjiJihuaAndHushuList = query.list();
		Object[] object=(Object[])tongjiJihuaAndHushuList.get(0);
		Long countId=(Long)object[0];        
		Double normalHeatingChargeSum=(Double)object[1];
		Double actualSumChargeSum=(Double)object[2];
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		qunuanfeiTongjiView.setJihuaAll(format.format(normalHeatingChargeSum==null?0d:normalHeatingChargeSum));
		qunuanfeiTongjiView.setShijiaoAllCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得全额收费(例),全额收费额
	 */
	@Override
	public void getQunuanfeiTongjiQuanE(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select count(t.id) as id,sum(t.actualSumCharge) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.preferential=:preferential "// 优惠金额=0
				+ " and t.livingSohard=:livingSohard"// 困难住户：false
				+ " and t.stopped=:stopped";// 停供的：false
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		query.setDouble("preferential", 0);
		query.setBoolean("livingSohard", false);
		query.setBoolean("stopped", false);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List quanEShoufeiList = query.list();
		Object[] object=(Object[])quanEShoufeiList.get(0);
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Long countId=(Long)object[0];
		Double actualSumChargeSum=(Double)object[1];
		qunuanfeiTongjiView.setQuanECount(countId);
		
		qunuanfeiTongjiView.setQuanECharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得惠后收费(例),惠后收费额
	 */
	@Override
	public void getQunuanfeiTongjiHuihou(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select count(t.id) as id,sum(t.actualSumCharge) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.preferential<>:preferential ";// 优惠金额 is not null
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		query.setDouble("preferential", 0);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List huihouShoufeiList = query.list();
		Object[] object=(Object[])huihouShoufeiList.get(0);
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Long countId=(Long)object[0];        
		Double actualSumChargeSum=(Double)object[1];
		qunuanfeiTongjiView.setHuihouCount(countId);
		qunuanfeiTongjiView.setHuihouCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得停供收费(例),停供收费额
	 */
	@Override
	public void getQunuanfeiTongjiTinggong(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select count(t.id) as id,sum(t.actualSumCharge) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.stopped=:stopped";// 停供的：true
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 停供
		query.setBoolean("stopped", true);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tinggongShoufeiList = query.list();
		Object[] object=(Object[])tinggongShoufeiList.get(0);
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Long countId=(Long)object[0];        
		Double actualSumChargeSum=(Double)object[1];
		qunuanfeiTongjiView.setTinggongCount(countId);
		qunuanfeiTongjiView.setTinggongCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得困难住户收费(例),困难住户收费额
	 */
	@Override
	public void getQunuanfeiTongjiKunnan(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select count(t.id) as id,sum(t.actualSumCharge) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.livingSohard=:livingSohard";// 困难住户：true
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 困难住户
		query.setBoolean("livingSohard", true);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List kunnanShoufeiList = query.list();
		Object[] object=(Object[])kunnanShoufeiList.get(0);
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Long countId=(Long)object[0];        
		Double actualSumChargeSum=(Double)object[1];
		qunuanfeiTongjiView.setKunnanCount(countId);
		qunuanfeiTongjiView.setKunnanCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得滞纳缴费(例),滞纳缴费额
	 */
	@Override
	public void getQunuanfeiTongjiZhinajin(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select count(t.id) as id,sum(t.actualSumCharge) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.zhinajinOn=:zhinajinOn";// 收滞纳金：true
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 收滞纳金
		query.setBoolean("zhinajinOn", true);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List zhinajinShoufeiList = query.list();
		Object[] object=(Object[])zhinajinShoufeiList.get(0);
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Long countId=(Long)object[0];        
		Double actualSumChargeSum=(Double)object[1];
		qunuanfeiTongjiView.setZhinajinCount(countId);
		qunuanfeiTongjiView.setZhinajinCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得优惠减免额
	 */
	@Override
	public void getQunuanfeiTongjiYouhuiJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(优惠金额)
		String hql = "select sum(t.preferential) as preferential "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.preferential is not null ";// 优惠金额 is not null
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List youhuiJianmianList = query.list();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Double preferentialSum=(Double)youhuiJianmianList.get(0);
		qunuanfeiTongjiView.setYouhuiJianmianChargeDouble(preferentialSum==null?0d:preferentialSum);
		qunuanfeiTongjiView.setYouhuiJianmianCharge(format.format(preferentialSum==null?0d:preferentialSum));
		return;
	}

	/**
	 * 取得停供减免额
	 */
	@Override
	public void getQunuanfeiTongjiTinggongJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {

		// sum(实际收取的合计)
		String hql = "select sum(t.normalHeatingCharge * (1-t.paymentDate.stopHeatingRate)) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.stopped=:stopped";// 停供的：true
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 停供
		query.setBoolean("stopped", true);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List tinggongJianmianList = query.list();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Double actualSumChargeSum=(Double)tinggongJianmianList.get(0);
		qunuanfeiTongjiView.setTinggongJianmianChargeDouble(actualSumChargeSum==null?0d:actualSumChargeSum);
		qunuanfeiTongjiView.setTinggongJianmianCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		return;
	}

	/**
	 * 取得困难住户减免额
	 */
	@Override
	public void getQunuanfeiTongjiKunnanJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		// sum(实际收取的合计)
		String hql = "select sum(t.normalHeatingCharge * (1-t.paymentDate.livingSoHardRate)) as actualSumCharge "
				+ " from HpsHeatingChargeRecord t where t.chargeState=:chargeState "
				+ " and t.livingSohard=:livingSohard";// 困难住户：true
		// 查询条件
		hql = hql + whereCondition(param);
		Query query = createQuery(hql);
		// 缴费状态：已缴费
		query.setParameter("chargeState", ChargeStateEnum.CHARGED);
		// 困难住户
		query.setBoolean("livingSohard", true);
		// 查询条件的参数设置
		setWhereConditionParam(param, query);
		List kunnanJianmianList = query.list();
		NumberFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		Double actualSumChargeSum=(Double)kunnanJianmianList.get(0);
		qunuanfeiTongjiView.setKunnanJianmianChargeDouble(actualSumChargeSum==null?0d:actualSumChargeSum);
		qunuanfeiTongjiView.setKunnanJianmianCharge(format.format(actualSumChargeSum==null?0d:actualSumChargeSum));
		qunuanfeiTongjiView.setJianmianAllCharge(format.format(g_jianmianAllCharge==null?0d:g_jianmianAllCharge));
		return;
	}
	
	private String whereCondition(QunuanfeiTongjiQueryParam param) {
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
	
	private void setWhereConditionParam(QunuanfeiTongjiQueryParam param, Query query) {
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
