package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsHouseDao extends GenericDao<HpsHouse, Long>{
	
	PaginationResult<HpsHouse> getHouses(HouseQueryParam queryParam);
	HpsHouse getHouse(Long houseId);
	HpsHouse saveHouse(HpsHouse house);
	List<HpsHouseOwner> getHouseOwners(String name, String idCardNo);
	void removeHouse(Long ... houseIds);
	HpsHouse getDuplicateHouse(HpsHouse house);

}
