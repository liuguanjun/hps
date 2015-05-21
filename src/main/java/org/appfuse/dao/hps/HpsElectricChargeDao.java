package org.appfuse.dao.hps;

import java.util.Collection;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.ElectricChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.PaginationQueryParam;
import com.my.hps.webapp.controller.vo.HpsElectricUserTongjiRowView;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsElectricChargeDao extends GenericDao<HpsElectricChargeRecord, Long> {
	
	/**
	 * 获取参数指定房屋的抄表记录
	 * 
	 * @param houseIds
	 * @return
	 */
	List<HpsElectricChaobiao> getChaobiaos(Collection<Long> houseIds);
	
	PaginationResult<HpsHouse> getHouses(ElectricChargeQueryParam queryParam);
	
	PaginationResult<HpsElectricChargeRecord> getChargeRecords(Long houseId, PaginationQueryParam param);

//	List<HpsElectricUserTongjiRowView> getUserTongjiRowList(ElectricTongjiQueryParam param);
	
	PaginationResult<HpsElectricChargeRecord> getChargeRecords(
			ElectricTongjiQueryParam param);

}
