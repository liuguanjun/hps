package org.appfuse.dao.hps.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsElectricUnitDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.ElectricUnitQueryParam;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.PaginationResult;

@Repository
public class HpsElectricUnitDaoHibernate extends HpsGenericDaoHibernate<HpsElectricUnit, Long> 
		implements HpsElectricUnitDao {

	public HpsElectricUnitDaoHibernate() {
		super(HpsElectricUnit.class);
	}

	@Override
	public PaginationResult<HpsElectricUnit> getUnits(
			ElectricUnitQueryParam param) {
		String hql = "from HpsElectricUnit where history = :history ";
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		hqlParams.put("history", false);
		String yongfangXingzhi = param.getYongfangXingzhiCode();
		if (StringUtils.isNotEmpty(yongfangXingzhi)) {
			hql += " and yongfangXingzhi.code = :yongfangXingzhi";
			hqlParams.put("yongfangXingzhi", yongfangXingzhi);
		}
		String baseCode = param.getBaseCode();
		if (StringUtils.isNotEmpty(baseCode)) {
			hql += " and base.code = :baseCode";
			hqlParams.put("baseCode", baseCode);
		}
		String areaCode = param.getAreaCode();
		if (StringUtils.isNotEmpty(areaCode)) {
			hql += " and area.code = :areaCode";
			hqlParams.put("areaCode", areaCode);
		}
		String louzuoCode = param.getLouzuoCode();
		if (StringUtils.isNotEmpty(louzuoCode)) {
			hql += "and louzuo.code = :louzuoCode";
			hqlParams.put("louzuoCode", louzuoCode);
		}
		String danyuan = param.getDanyuan();
		if (StringUtils.isNotEmpty(danyuan)) {
			hql += "and danyuan = :danyuan";
			hqlParams.put("danyuan", danyuan);
		}
		String ceng = param.getCeng();
		if (StringUtils.isNotEmpty(ceng)) {
			hql += "and ceng = :ceng";
			hqlParams.put("ceng", ceng);
		}
		String doorNo = param.getDoorNo();
		if (StringUtils.isNotEmpty(doorNo)) {
			hql += "and doorNo = :doorNo";
			hqlParams.put("doorNo", doorNo);
		}
		Query query = createQuery(hql);
		Query queryCnt = createQuery("select count (id) " + hql);
		for (Map.Entry<String, Object> paramEntry : hqlParams.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(param.getOffset());
		query.setMaxResults(param.getRows());
		@SuppressWarnings("unchecked")
		List<HpsElectricUnit> records = query.list();
		PaginationResult<HpsElectricUnit> result = new PaginationResult<HpsElectricUnit>();
		result.setRows(records);
		Long cntLong = (Long) queryCnt.uniqueResult();
		result.setTotal(cntLong.intValue());
		return result;
	}


}
