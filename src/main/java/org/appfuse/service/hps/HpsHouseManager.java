package org.appfuse.service.hps;

import java.util.List;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsHouseManager {
	
	PaginationResult<HpsHouse> getHouses(HouseQueryParam queryParam);
	HpsHouse getHouse(Long houseId);
	HpsHouse saveHouse(HpsHouse house);
	List<HpsHouseOwner> getHouseOwners(String name, String idCardNo);
	void removeHouse(Long ... houseIds);

}
