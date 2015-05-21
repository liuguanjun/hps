package org.appfuse.dao.hps.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsHouseDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.dataauth.HpsDataAuthorization;
import com.my.hps.webapp.dataauth.HpsDataAuthorization.TargetType;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.PaginationResult;

@Repository
public class HpsHouseDaoHibernate extends HpsGenericDaoHibernate<HpsHouse, Long> implements HpsHouseDao{

	public HpsHouseDaoHibernate() {
		super(HpsHouse.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@HpsDataAuthorization(type = TargetType.Query, attrName = "louzuo.area.base.id")
	public PaginationResult<HpsHouse> getHouses(HouseQueryParam queryParam) {
		String sql = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (queryParam == null || queryParam.isEmpty()) {
			sql = "from HpsHouse";
		} else {
			sql = "from HpsHouse where 1 = 1 ";
			String baseCode = queryParam.getBaseCode();
			String areaCode = queryParam.getAreaCode();
			if (StringUtils.isNotEmpty(baseCode)) {
				// 基地有选择
				sql += "and louzuo.area.base.code = :baseCode ";
				params.put("baseCode", baseCode);
			}
			if (StringUtils.isNotEmpty(areaCode)) {
				// 区域有选择
				sql += "and louzuo.area.code = :areaCode ";
				params.put("areaCode", areaCode);
			}
			String louzuoCode = queryParam.getLouzuoCode();
			if (StringUtils.isNotEmpty(louzuoCode)) {
				sql += " and louzuo.code = :louzuoCode";
				params.put("louzuoCode", louzuoCode);
			}
			String danyuan = queryParam.getDanyuan();
			if (StringUtils.isNotEmpty(danyuan)) {
				sql += " and danyuan = :danyuan";
				params.put("danyuan", danyuan);
			}
			String ceng = queryParam.getCeng();
			if (StringUtils.isNotEmpty(ceng)) {
				sql += " and ceng = :ceng";
				params.put("ceng", ceng);
			}
			String ownerName = queryParam.getOwnerName();
			if (StringUtils.isNotEmpty(ownerName)) {
				sql += " and owner.name like :ownerName";
				params.put("ownerName", "%" + ownerName + "%");
			}
			String shenfenXingzhiCode = queryParam.getShenfenXingzhiCode();
			if (StringUtils.isNotEmpty(shenfenXingzhiCode)) {
				sql += " and shenfenXingzhi.code = :shenfenXingzhiCode";
				params.put("shenfenXingzhiCode", shenfenXingzhiCode);
			}
			String gongshangNo = queryParam.getGongshangNo();
			if (StringUtils.isNotEmpty(gongshangNo)) {
				sql += " and gongshangNo like :gongshangNo";
				params.put("gongshangNo", "%" + gongshangNo + "%");
			}
			String yongfangXingzhiCode = queryParam.getYongfangXingzhiCode();
			if (StringUtils.isNotEmpty(yongfangXingzhiCode)) {
				sql += " and yongfangXingzhi.code = :yongfangXingzhiCode";
				params.put("yongfangXingzhiCode", yongfangXingzhiCode);
			}
			String ownerNo = queryParam.getOwnerNo();
			if (StringUtils.isNotEmpty(ownerNo)) {
				sql += " and owner.no like :ownerNo";
				params.put("ownerNo", "%" + ownerNo + "%");
			}
			String houseNo = queryParam.getHouseNo();
			if (StringUtils.isNotEmpty(houseNo)) {
				sql += " and no like :houseNo";
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
		}
		Query query = createQuery(sql);
		Query queryCnt = createQuery("select count (id) " + sql);
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
			queryCnt.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		query.setFirstResult(queryParam.getOffset());
		query.setMaxResults(queryParam.getRows());
		List<HpsHouse> houses = query.list();
		PaginationResult<HpsHouse> result = new PaginationResult<HpsHouse>();
		result.setRows(houses);
		Long cntLong = (Long) queryCnt.uniqueResult();
		result.setTotal(cntLong.intValue());
		return result;
	}

	@Override
	public HpsHouse getHouse(Long houseId) {
		return get(houseId);
	}

	@Override
	public HpsHouse saveHouse(HpsHouse house) {
		saveOrUpdate(house.getOwner());
		save(house);
		return house;
	}

	@Override
	public List<HpsHouseOwner> getHouseOwners(String name, String idCardNo) {
		String sql = "from HpsHouseOwner  where 1 = 1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(name)) {
			sql += " and name = :name";
			params.put("name", name);
		}
		if (StringUtils.isNotEmpty(idCardNo)) {
			sql += " and idCardNo = :idCardNo";
			params.put("idCardNo", idCardNo);
		}
		Query query = createQuery(sql);
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		@SuppressWarnings("unchecked")
		List<HpsHouseOwner> owners = query.list();
		return owners;
	}

	@Override
	public void removeHouse(Long... houseIds) {
		for (Long houseId : houseIds) {
			HpsHouse house = get(houseId);
			Query deleteElectricQuery = createQuery("delete from HpsElectricChaobiao where house.id = :houseId");
			Query deleteHeatingQuery = createQuery("delete from HpsHeatingChargeRecord where house.id = :houseId");
			Query deleteMaintainQuery = createQuery("delete from HpsMaintainChargeRecord where house.id = :houseId");
			deleteElectricQuery.setParameter("houseId", houseId);
			deleteHeatingQuery.setParameter("houseId", houseId);
			deleteMaintainQuery.setParameter("houseId", houseId);
			deleteElectricQuery.executeUpdate();
			deleteHeatingQuery.executeUpdate();
			deleteMaintainQuery.executeUpdate();
			house.getOwner().getHouses().remove(house);
			house.setOwner(null);
			remove(houseId);
		}
	}

	@Override
	public HpsHouse getDuplicateHouse(HpsHouse house) {
		String sql = "from HpsHouse where louzuo.id = :louzuoId and "
				+ "danyuan = :danyuan and ceng = :ceng and doorNo = :doorNo";
		if (house.getId() != null) {
			sql += " and id != :id";
		}
		Query query = createQuery(sql);
		query.setParameter("louzuoId", house.getLouzuo().getId());
		query.setParameter("danyuan", house.getDanyuan());
		query.setParameter("ceng", house.getCeng());
		query.setParameter("doorNo", house.getDoorNo());
		if (house.getId() != null) {
			query.setParameter("id", house.getId());
		}
		return (HpsHouse) query.uniqueResult();
	}

}
