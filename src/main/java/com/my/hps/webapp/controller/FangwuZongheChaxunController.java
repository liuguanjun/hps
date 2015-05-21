package com.my.hps.webapp.controller;

import java.util.List;

import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.hps.HpsHouseManager;
import org.appfuse.service.hps.HpsLouzuoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.HouseQueryParam;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.HpsLouzuo;
import com.my.hps.webapp.model.PaginationResult;

@Controller
@RequestMapping("/admin/basedata/")
public class FangwuZongheChaxunController extends BaseFormController {
	
	private HpsHouseManager houseManager;
	private HpsLouzuoManager louzuoManager;
	private HpsDictManager dictManager;
	
	@RequestMapping(method=RequestMethod.GET,value="houses")
	@ResponseBody
	public PaginationResult<HpsHouse> getAllHouses(@ModelAttribute HouseQueryParam queryParam) {
		return houseManager.getHouses(queryParam);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="house")
	@ResponseBody
	public HpsHouse addHouse(@ModelAttribute HpsHouse house) {
		setCodeItems(house);
		HpsHouseOwner owner = house.getOwner();
		String ownerName = owner.getName();
		String idCardNo = owner.getIdCardNo();
		List<HpsHouseOwner> ownerSameListInDB = getHouseOwners(ownerName,
				idCardNo);
		int duplicatedOwnerSize = ownerSameListInDB.size();
		if (duplicatedOwnerSize > 1) {
			throw new RuntimeException("发现" + ownerSameListInDB.size()
					+ "个重复的房主[" + ownerName + "]");
		}
		if (duplicatedOwnerSize == 1) {
			HpsHouseOwner ownerSameInDB = ownerSameListInDB.get(0);
			house.setOwner(ownerSameInDB);
			ownerSameInDB.setPhoneNo(owner.getPhoneNo());
			ownerSameInDB.setWageNum(owner.getWageNum());
			ownerSameInDB.setRemarks(owner.getRemarks());
		} else { 
			// 没有房主信息时，在saveHouse时，会保存新的户主信息
		}
		return houseManager.saveHouse(house);
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="house")
	@ResponseBody
	public HpsHouse updateHouse(@ModelAttribute HpsHouse house) {
		setCodeItems(house);
		return houseManager.saveHouse(house);
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="changeHouse")
	@ResponseBody
	public HpsHouse changeHouseName(@ModelAttribute HpsHouse house) {
//		setCodeItems(house);
		HpsHouseOwner owner = house.getOwner();
		String ownerName = owner.getName();
		String idCardNo = owner.getIdCardNo();
		List<HpsHouseOwner> ownerSameListInDB = getHouseOwners(ownerName,
				idCardNo);
		int duplicatedOwnerSize = ownerSameListInDB.size();
		if (duplicatedOwnerSize > 1) {
			throw new RuntimeException("发现" + ownerSameListInDB.size()
					+ "个重复的房主[" + ownerName + "]");
		}
		if (duplicatedOwnerSize == 1) {
			HpsHouseOwner ownerSameInDB = ownerSameListInDB.get(0);
			for (HpsHouse ownerHouse : ownerSameInDB.getHouses()) {
				if (ownerHouse.getId().equals(house.getId())) {
					// 并没有实际更名，直接点击保存
					return ownerHouse;
				}
			}
			ownerSameInDB.setPhoneNo(owner.getPhoneNo());
			ownerSameInDB.setWageNum(owner.getWageNum());
			ownerSameInDB.setRemarks(owner.getRemarks());
			owner = ownerSameInDB;
		} else { 
			// 没有房主信息时，在saveHouse时，会保存新的户主信息
		}
		HpsHouse houseInDB = houseManager.getHouse(house.getId());
		// 设置身份性质（房屋更名时，一般都会修改身份性质）
		String shenfenXingzhiCode = house.getShenfenXingzhi().getCode();
		HpsDictItem shenfenXingzhiItem = dictManager.getShenfenXingzhiDictItem(shenfenXingzhiCode);
		houseInDB.setShenfenXingzhi(shenfenXingzhiItem);
		houseInDB.setOwner(owner);
		houseManager.saveHouse(houseInDB);
		return houseInDB;
	}
	
	@RequestMapping(method=RequestMethod.DELETE,value="house/{houseIds}")
	@ResponseBody
	public void deleteHouse(@PathVariable String houseIds) {
		String[] houseIdStrArray = houseIds.split(",");
		Long[] houseIdLongArray = new Long[houseIdStrArray.length];
		int i = 0;
		for (String houseIdStr : houseIdStrArray) {
			houseIdLongArray[i++] = Long.valueOf(houseIdStr);
		}
		houseManager.removeHouse(houseIdLongArray);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="house/{houseId}")
	@ResponseBody
	public HpsHouse getHouse(@PathVariable Long houseId) {
		return houseManager.getHouse(houseId);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="houseowners")
	@ResponseBody
	public List<HpsHouseOwner> getHouseOwners(@RequestParam String name, @RequestParam String idCardNo) {
		return houseManager.getHouseOwners(name, idCardNo);
	}

	@Autowired
	public void setHouseManager(HpsHouseManager houseManager) {
		this.houseManager = houseManager;
	}
	
	@Autowired
	public void setLouzuoManager(HpsLouzuoManager louzuoManager) {
		this.louzuoManager = louzuoManager;
	}
	
	@Autowired
	public void setDictManager(HpsDictManager dictManager) {
		this.dictManager = dictManager;
	}

	/**
	 * 通过code获取楼座、用房性质、身份性质并重新设置到house中，
	 */
	private void setCodeItems(HpsHouse house) {
		// 楼座
		String areaCode = house.getLouzuo().getArea().getCode();
		List<HpsLouzuo> louzuoList = louzuoManager.getHpsLouzuoByCode(areaCode);
		String louzuoCode = house.getLouzuo().getCode();
		HpsLouzuo louzuoFound = null;
		for (HpsLouzuo houzuo : louzuoList) {
			if (louzuoCode.equals(houzuo.getCode())) {
				louzuoFound = houzuo;
				break;
			}
		}
		house.setLouzuo(louzuoFound);
		// 用房性质
		String yongfangXingzhiCode = house.getYongfangXingzhi().getCode();
		HpsDictItem yongfangXingzhiItem = dictManager.getYongfangXingzhiDictItem(yongfangXingzhiCode);
		house.setYongfangXingzhi(yongfangXingzhiItem);
		// 身份性质
		String shenfenXingzhiCode = house.getShenfenXingzhi().getCode();
		HpsDictItem shenfenXingzhiItem = dictManager.getShenfenXingzhiDictItem(shenfenXingzhiCode);
		house.setShenfenXingzhi(shenfenXingzhiItem);
	}

}
