package com.my.hps.dataimport;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsHeatingPaymentDate;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;
import com.my.hps.webapp.model.HpsHeatingUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.HpsLouzuo;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsMaintainPaymentDate;
import com.my.hps.webapp.model.HpsMaintainUnit;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.ChargeStateEnum;
import com.my.hps.webapp.model.enums.HpsUserType;
import com.my.hps.webapp.model.enums.Sex;

public class DataImportMainWeb implements PreInsertEventListener, PreUpdateEventListener {
	
//	private Session sessionVB;
	private Session sessionWeb;
	private Session session;
	private Date currentDate = new Date();
	
	public static void main(String[] args) throws Exception {
		DataImportMainWeb main = new DataImportMainWeb();
		main.setSession();
		main.clearData();
		main.importHpsData();
	}
	
	private void setSession() {
		// 新建系统
		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.1.xml")
				.buildSessionFactory();
		session = sessionFactory.openSession();
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(
        EventListenerRegistry.class);
//        registry.getEventListenerGroup(EventType.MERGE).appendListener(mergeListener);
//        registry.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener(saveOrUpdateListener);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(this);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(this);
		// 老系统(VB)
//		SessionFactory sessionFactoryVB = new Configuration().configure("/hibernate.cfg.vb.xml")
//				.buildSessionFactory();
//		sessionVB = sessionFactory.openSession();
		// 老系统(Web)
		SessionFactory sessionFactoryWeb = new Configuration().configure("/hibernate.cfg.web.xml")
				.buildSessionFactory();
		sessionWeb = sessionFactoryWeb.openSession();
	}
	
	private void releaseSession() {
		session.close();
//		sessionVB.close();
		sessionWeb.close();
	}
	
	private void clearData() {
		session.createSQLQuery("delete from hps_maintain_charge_record").executeUpdate();
		session.createSQLQuery("delete from hps_maintain_unit").executeUpdate();
		session.createSQLQuery("delete from hps_maintain_payment_date").executeUpdate();
		session.createSQLQuery("delete from hps_heating_charge_record").executeUpdate();
		session.createSQLQuery("delete from hps_heating_payment_date_preferential").executeUpdate();
		session.createSQLQuery("delete from hps_heating_shenfen_xingzhi_preferential").executeUpdate();
		session.createSQLQuery("delete from hps_heating_unit").executeUpdate();
		session.createSQLQuery("delete from hps_heating_payment_date").executeUpdate();
		session.createSQLQuery("delete from user_role where user_id != 1").executeUpdate();
		session.createSQLQuery("delete from hps_user where id > 1").executeUpdate();
		session.createSQLQuery("delete from app_user where id > 1").executeUpdate();
		session.createSQLQuery("delete from hps_house").executeUpdate();
		session.createSQLQuery("delete from hps_house_owner").executeUpdate();
		session.createSQLQuery("delete from hps_louzuo").executeUpdate();
		session.createSQLQuery("delete from hps_area").executeUpdate();
		session.createSQLQuery("delete from hps_base").executeUpdate();
//		session.createSQLQuery("delete from hps_dictitem").executeUpdate();
//		session.createSQLQuery("delete from hps_dict").executeUpdate();
	}
	
	private void importHpsData() {
		Transaction t = session.beginTransaction();
		importHpsBaseData();
		importHpsUsers();
		importHpsYongfangXingzhiData();
		importHpsShefenXingzhiData();
		t.commit();
		t = session.beginTransaction();
		importHpsHouseAndOwnerData();
		importHpsHeatingPaymentDate();
		importHpsHeatingPaymentDatePreferential();
		importHpsHeatingShefenXingzhiPreferentail();
		importHpsHeatingUnit();
		t.commit();
		t = session.beginTransaction();
		importHpsHeatingChargeRecord();
		t.commit();
		t = session.beginTransaction();
		importMaintainPaymentDate();
		importMaintainUnit();
		importMaintainChargeRecord();
		t.commit();
		System.out.println("------------------OK-----------------");
	}
	
	private Map<Integer, HpsBase> hpsBaseMap = new HashMap<Integer, HpsBase>();
	private Map<Integer, HpsArea> hpsAreaMap = new HashMap<Integer, HpsArea>();
	
	private String[] orderAreaArray = {"河东",
			"河西",
			"新园",
			"兴南",
			"城南",
			"城东",
			"高中改建楼",
			"河东平房居民",
			"河东商网点",
			"河西商网点",
			"口前商业点",
			"商业网点",
			"河西商网点中居民",
			"临时",
			"改建"};
	
	// 导入基地和区域数据
	private void importHpsBaseData() {
		String qBaseStr = "SELECT TOP 1000 [stationID]"
				+ "      ,[stationName]"
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[station]";
		String qAreaStr = "SELECT TOP 1000 [areaID]"
				+ "      ,[stationID]"
				+ "      ,[areaName]"
				+ "  FROM [commonDB].[dbo].[area] where stationID = :stationID";
		SQLQuery qBase = sessionWeb.createSQLQuery(qBaseStr);
		@SuppressWarnings("unchecked")
		List<Object[]> stationAttrArrayList = qBase.list();
		int ib = 1;
		for (Object[] stationAttrArray : stationAttrArrayList) {
			Integer stationId = (Integer) stationAttrArray[0];
			String stationName = (String) stationAttrArray[1];
			String stationCode = "BASE_" + ib++;
			HpsBase base = new HpsBase();
			base.setCode(stationCode);
			base.setName(stationName);
			SQLQuery qArea = sessionWeb.createSQLQuery(qAreaStr);
			qArea.setInteger("stationID", stationId);
			@SuppressWarnings("unchecked")
			List<Object[]> areaAttrArrayList = qArea.list();
			if (stationName.startsWith("口前")) {
				Collections.sort(areaAttrArrayList, new Comparator<Object[]>() {
					
					@Override
					public int compare(Object[] o1, Object[] o2) {
						String areaName1 = (String) o1[2];
						String areaName2 = (String) o2[2];
						int index1 = ArrayUtils.indexOf(orderAreaArray, areaName1);
						int index2 = ArrayUtils.indexOf(orderAreaArray, areaName2);
						return index1 - index2;
					}
				});
			}
			Set<HpsArea> areaSet = new LinkedHashSet<HpsArea>();
			int ia = 101;
			
			for (Object[] areaAttrArray : areaAttrArrayList) {
				Integer areaId = (Integer) areaAttrArray[0];
				String areaName = (String) areaAttrArray[2];
				String areaCode = stationCode + "_AREA_" + String.valueOf(ia++).substring(1);
				HpsArea area = new HpsArea();
				area.setName(areaName);
				area.setCode(areaCode);
				area.setBase(base);
				area.setSystemInner(true);
				areaSet.add(area);
				hpsAreaMap.put(areaId, area);
			}
			base.setAreaSet(areaSet);
			session.save(base);
			hpsBaseMap.put(stationId, base);
		}
		for (Map.Entry<Integer, HpsArea> hpsAreaEntry : hpsAreaMap.entrySet()) {
			// 导入楼座
			importHpsLouzuoData(hpsAreaEntry.getKey(), hpsAreaEntry.getValue());
		}
	}
	
	private Map<Integer, HpsLouzuo> hpsLouzuoMap = new HashMap<Integer, HpsLouzuo>();
	
	// 导入楼座数据
	private void importHpsLouzuoData(Integer oldAreaId, HpsArea area) {
		String qBSRStr = "SELECT TOP 1000 [BSRID]"
				+ "      ,[areaID]"
				+ "      ,[BSRName]"
				+ "  FROM [commonDB].[dbo].[BSR] where areaId = :areaId";
		SQLQuery qBSR = sessionWeb.createSQLQuery(qBSRStr);
		qBSR.setInteger("areaId", oldAreaId);
		@SuppressWarnings("unchecked")
		List<Object[]> louzuoAttrArrayList = qBSR.list();
		int i = 1;
		for (Object[] louzuoAttrArray : louzuoAttrArrayList) {
			Integer louzuoId = (Integer) louzuoAttrArray[0];
			Integer areaId = (Integer) louzuoAttrArray[1];
			String louzuoName = (String) louzuoAttrArray[2];
			HpsLouzuo louzuo = new HpsLouzuo();
			louzuo.setArea(area);
			louzuo.setCode(area.getCode() + "_LOUZUO_" + i++);
			louzuo.setName(louzuoName);
			louzuo.setSystemInner(true);
			session.save(louzuo);
			hpsLouzuoMap.put(louzuoId, louzuo);
		}
		
	}
	
	private HpsDictItem personalItem;
	private HpsDictItem commercialItem;
	
	// 用房性质
	private void importHpsYongfangXingzhiData() {
		// 个人
		personalItem = (HpsDictItem) session.get(HpsDictItem.class, 6l);
		// 商用
		commercialItem = (HpsDictItem) session.get(HpsDictItem.class, 7l);
	}
	
	private Map<Integer, HpsDictItem> shenfenXingzhiMap = new HashMap<Integer, HpsDictItem>();
	private Map<String, HpsDictItem> shenfenXingzhiNameMap = new HashMap<String, HpsDictItem>();
	
	// 导入身份性质数据
	private void importHpsShefenXingzhiData() {
		session.createSQLQuery("delete from hps_dictitem where code like 'SHENFEN_XINGZHI_%'")
			.executeUpdate();
		HpsDict shenfenXingzhiDict = (HpsDict) session.get(HpsDict.class, 1l);
		String qStatusStr = "SELECT TOP 1000 [statusID]"
				+ "      ,[statusName]"
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[status] order by statusID";
		SQLQuery qStatus = sessionWeb.createSQLQuery(qStatusStr);
		@SuppressWarnings("unchecked")
		List<Object[]> itemAttrArrayList = qStatus.list();
//		Set<HpsDictItem> itemSet = new LinkedHashSet<HpsDictItem>();
		int i = 1;
		for (Object[] itemAttrArray : itemAttrArrayList) {
			Integer statusId = (Integer) itemAttrArray[0];
			String statusName = (String) itemAttrArray[1];
			HpsDictItem item = new HpsDictItem();
			item.setCode(shenfenXingzhiDict.getCode() + "_" + i++);
			item.setName(statusName);
			item.setDict(shenfenXingzhiDict);
			session.save(item);
//			itemSet.add(item);
			shenfenXingzhiMap.put(statusId, item);
			shenfenXingzhiNameMap.put(statusName, item);
		}
//		shenfenXingzhiDict.setItemSet(itemSet);
//		session.save(shenfenXingzhiDict);
	}
	
	private Map<Integer, HpsHouse> hpsHouseMap = new HashMap<Integer, HpsHouse>();
	
	// 导入房屋数据
	// 1. 一个房主可能有多套房产，每套房产有不同的身份性质
	// 2. 有的房产可能不需要缴纳取暖费，applyHeating = 0 =>新系统设置取暖费面积为0
	// 3. 有的户主在缴纳取暖费时，录入的户主名称与实际房屋的房主名称不一致，此次导入已缴费时名称为准，并且在备注中标记
	//  在客户现场去进行调整。
	// 4. 有重名的户主，可通过判断工资号是否相等来判断是否重名，对于有重名的户主来说，在备注中做标记，安装现场时去调整
	private void importHpsHouseAndOwnerData() {
		String qHouseholdStr = "SELECT [householdID]"
				+ "      ,[BSRID]"
				+ "      ,[unitNum]"
				+ "      ,[floorNum]"
				+ "      ,[doorNum]"
				+ "      ,[ammeterNum]" // 老系统没有使用
				+ "      ,[sharerName]" // 新系统没有这个字段
				+ "      ,[heatingAcreage]"
				+ "      ,[houseMaintainAcreage]"
				+ "      ,[isOldHouse]" // 新系统没有这个字段
				+ "      ,[isCommercial]" // 10
				+ "      ,[applyHeating]" // 新系统没有这个字段，如果不缴纳取暖费，取暖面积写0就可以了 11
				+ "      ,[applyElectric]"// 新系统没有这个字段 12
				+ "      ,[applyHouseMaintain]"// 新系统没有这个字段 13
				+ "      ,[applyClean]"// 新系统没有这个字段 14
				+ "      ,[applyCorridorLight]"// 新系统没有这个字段 15
				+ "      ,[electricDeposit]"// 新系统没有这个字段 16
				+ "      ,[masterName]" // 17
				+ "      ,[statusID]" // 身份性质，18
				+ "      ,[sex]"// 19, 新系统无此字段
				+ "      ,[phone]" // 20
				+ "      ,[wageNum]" // 21
				+ "      ,[remarks]" // 22
				+ "  FROM [commonDB].[dbo].[household]";
		SQLQuery qHousehold = sessionWeb.createSQLQuery(qHouseholdStr);
		
		List<HpsHouseOwner> owners = new ArrayList<HpsHouseOwner>();
		@SuppressWarnings("unchecked")
		List<Object[]> householdAttrArrayList = qHousehold.list();
		// 获取户主信息
		for (Object[] householdAttrArray : householdAttrArrayList) {
			String masterName = (String) householdAttrArray[17];
//			Integer statusId = (Integer) householdAttrArray[18];
			String sex = (String) householdAttrArray[19];
			String phoneNo = (String) householdAttrArray[20];
			String wageNum = (String) householdAttrArray[21];
			String remarks = (String) householdAttrArray[22];
			HpsHouseOwner owner = new HpsHouseOwner();
			owner.setName(masterName);
			owner.setPhoneNo(phoneNo);
			owner.setRemarks(remarks);
			owner.setWageNum(wageNum);
			owner.setHouses(new HashSet<HpsHouse>());
			owners.add(owner);
		}
		// 去重复
		deleteDuplicatedOwners(owners);
		// 获取房屋信息
		for (Object[] householdAttrArray : householdAttrArrayList) {
			Integer householdId = (Integer) householdAttrArray[0];
			Integer bsrId = (Integer) householdAttrArray[1];
			String danyuan = (String) householdAttrArray[2];
			String ceng = (String) householdAttrArray[3];
			String doorNo = (String) householdAttrArray[4];
			BigDecimal warmArea = (BigDecimal) householdAttrArray[7];
			BigDecimal repairArea = (BigDecimal) householdAttrArray[8];
			// 个人、商用映射成新系统的用房性质
			Boolean isCommercial = (Boolean) householdAttrArray[10];
			HpsDictItem yongfangXingzhiItem = null;
			if (isCommercial) {
				yongfangXingzhiItem = commercialItem;
			} else {
				yongfangXingzhiItem = personalItem;
			}
			Integer statusId = (Integer) householdAttrArray[18];
			HpsHouse house = new HpsHouse();
			house.setCeng(ceng);
			house.setDanyuan(danyuan);
			house.setDoorNo(doorNo);
			HpsLouzuo louzuo = hpsLouzuoMap.get(bsrId);
			if (louzuo == null) {
				throw new RuntimeException("louzuo bsrID: " + bsrId + " not existent");
			}
			house.setYongfangXingzhi(yongfangXingzhiItem);
			house.setLouzuo(louzuo);
			HpsDictItem shenfengXingzhiItem = shenfenXingzhiMap.get(statusId);
			if (shenfengXingzhiItem == null) {
				throw new RuntimeException("shenfengXingzhiItem statusId: " + statusId + " not existent");
			}
			house.setShenfenXingzhi(shenfengXingzhiItem);
			house.setWarmArea(warmArea == null ? 0d : warmArea.doubleValue());
			house.setRepairArea(repairArea == null ? 0d : repairArea.doubleValue());
			String masterName = (String) householdAttrArray[17];
			String wageNum = (String) householdAttrArray[21];
			HpsHouseOwner owner = getHpsHouseOwner(owners, masterName, wageNum);
			if (owner == null) {
				throw new RuntimeException("owner masterName: " + masterName
						+ " wageNum: " + wageNum 
						+ " not existent");
			}
			house.setOwner(owner);
			owner.getHouses().add(house);
			hpsHouseMap.put(householdId, house);
		}
		for (HpsHouseOwner owner : owners) {
			session.save(owner);
			// 房主号
			owner.setNo(StringUtils.leftPad(owner.getId().toString(), 5, '0'));
			// 房号
			for (HpsHouse house : owner.getHouses()) {
				house.setNo(StringUtils.leftPad(house.getId().toString(), 5,
						'0'));
			}
		}
	}
	
	// 将重名的Owner删掉，并且在备注中增加“可能重名或者多套房产”
	private void deleteDuplicatedOwners(List<HpsHouseOwner> owners) {
		for (int i = 0; i < owners.size(); i++) {
			HpsHouseOwner owner = owners.get(i);
			String wageNum = owner.getWageNum();
			if (wageNum == null) {
				wageNum = "";
			}
			boolean maybeDuplicate = false;
			for (int j = i + 1; j < owners.size(); j++) {
				HpsHouseOwner owner2 = owners.get(j);
				if (owner.getName().equals(owner2.getName())) {
					// 姓名相等，可能是重名或者多套房产
					maybeDuplicate = true;
					String wageNum2 = owner2.getWageNum();
					if (wageNum2 == null) {
						wageNum2 = "";
					}
					if (wageNum2.equals(wageNum)) {
						// 工资号相等，很有可能是同一个人,删除这个户主
						owners.remove(owner2);
						j--;
					} else {
						// 工资号不相等，应该不是一个人，就是重名而已
						String remarks = owner2.getRemarks();
						if (remarks == null) {
							remarks = "";
						}
						remarks += "  可能重名或者多套房产<<<<<";
						owner2.setRemarks(remarks);
					}
				}
			}
			if (maybeDuplicate) {
				String remarks = owner.getRemarks();
				if (remarks == null) {
					remarks = "";
				}
				remarks += "  可能重名或者多套房产<<<<<";
				owner.setRemarks(remarks);
			}
		}
	}
	
	private HpsHouseOwner getHpsHouseOwner(List<HpsHouseOwner> owners, String name, String wageNum) {
		if (wageNum == null) {
			wageNum = "";
		}
		for (HpsHouseOwner owner : owners) {
			String wageNum2 = owner.getWageNum();
			if (wageNum2 == null) {
				wageNum2 = "";
			}
			if (owner.getName().equals(name) && wageNum2.equals(wageNum)) {
				return owner;
			}
		}
		throw new RuntimeException("can't find houseowner name : " + name + " and wageNum : " + wageNum);
	}
	
	private Map<Integer, HpsHeatingPaymentDate> hpsHeatingPaymentDateMap 
			= new HashMap<Integer, HpsHeatingPaymentDate>();
	
	// 导入取暖费缴纳日期
	private void importHpsHeatingPaymentDate() {
		String qPaymentDateStr = "SELECT TOP 1000 [paymentMainID]" // 0
				+ "      ,[stationID]" // 1
				+ "      ,[paymentTitle]"// 2
				+ "      ,[payStart]" //3
				+ "      ,[payStop]" // 4
				+ "      ,[startService]"// 5
				+ "      ,[stopService]" // 6
				+ "      ,[diverted]" // 7
				+ "      ,[remarks]" // 8
				+ "  FROM [commonDB].[dbo].[heatingPaymentMain]";
		SQLQuery qPaymentDate = sessionWeb.createSQLQuery(qPaymentDateStr);
		List<Object[]> dateAttrArrayList = qPaymentDate.list();
		for (Object[] itemAttrArray : dateAttrArrayList) {
			Integer paymentMainId = (Integer) itemAttrArray[0];
			Integer stationId = (Integer) itemAttrArray[1];
			String title = (String) itemAttrArray[2];
			Date payStart = (Date) itemAttrArray[3];
			Date payEnd = (Date) itemAttrArray[4];
			String remarks = (String) itemAttrArray[8];
			HpsHeatingPaymentDate paymentDate = new HpsHeatingPaymentDate();
			HpsBase base = hpsBaseMap.get(stationId);
			if (base == null) {
				throw new RuntimeException("can't find base : " + stationId);
			}
			paymentDate.setBase(base);
			paymentDate.setChargeRecordsInitialized(true);
			paymentDate.setPayStartDate(payStart);
			paymentDate.setPayEndDate(payEnd);
			paymentDate.setLivingSoHardRate(0.5d); // 困难户
			paymentDate.setStopHeatingRate(0.2d); // 停供比例
			paymentDate.setRemarks(remarks);
			paymentDate.setTitle(title);
			paymentDate.setZhinajinRate(0.003d); // 滞纳金
			session.save(paymentDate);
			hpsHeatingPaymentDateMap.put(paymentMainId, paymentDate);
		}
	}
	
	private Map<String, HpsHeatingPaymentDatePreferential> datePrefersMap
			= new HashMap<String, HpsHeatingPaymentDatePreferential>();
	
	// 导入取暖费缴纳日期优惠
	private void importHpsHeatingPaymentDatePreferential() {
		String qDatePreferStr = "SELECT TOP 1000 [heatingPreferentialsID]"
				+ "      ,[paymentMainID]"
				+ "      ,[preferentiaNeedsStatusName]"
				+ "      ,[preferentialTitle]"
				+ "      ,[preferentialValue]"
				+ "      ,[preferentialRate]"
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[heatingPreferentials] where preferentiaNeedsStatusName = '一般'";
		SQLQuery qDatePrefer = sessionWeb.createSQLQuery(qDatePreferStr);
		List<Object[]> preferAttrArrayList = qDatePrefer.list();
		for (Object[] preferAttrArray : preferAttrArrayList) {
			Integer paymentMainId = (Integer) preferAttrArray[1];
			HpsHeatingPaymentDate paymentDate = hpsHeatingPaymentDateMap.get(paymentMainId);
			if (paymentDate == null) {
				throw new RuntimeException("can't find heatingPaymentMain id:" + paymentMainId);
			}
			String statusName = (String) preferAttrArray[2];
			String title = (String) preferAttrArray[3];
			BigDecimal offRate = (BigDecimal) preferAttrArray[5];
			String remarks = (String) preferAttrArray[6];
			Date startDate = getStartDateFromTitle(paymentDate, title);
			Date endDate = getEndDateFromTitle(paymentDate, title);
			HpsHeatingPaymentDatePreferential prefer = new HpsHeatingPaymentDatePreferential();
			prefer.setStartDate(startDate);
			prefer.setEndDate(endDate);
			prefer.setPaymentDate(paymentDate);
			prefer.setRemarks(remarks);
			prefer.setOffRate(offRate.doubleValue());
			prefer.setTitle(title);
			session.save(prefer);
			datePrefersMap.put(paymentMainId + "_" + title, prefer);
		}
	}
	
	private Date getStartDateFromTitle(HpsHeatingPaymentDate paymentDate, String title) {
		String startMDStr = title.substring(0, title.indexOf("~"));
		int year = paymentDate.getPayStartDate().getYear() + 1900;
		String startDateStr = year + "/" + startMDStr;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			return format.parse(startDateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Date getEndDateFromTitle(HpsHeatingPaymentDate paymentDate, String title) {
		Date startDate = getStartDateFromTitle(paymentDate, title);
		String endMDStr = title.substring(title.indexOf("~") + 1, title.indexOf("日"));
		if (endMDStr.indexOf("/") < 0) { // 没有月
			endMDStr = (startDate.getMonth() + 1) + "/" + endMDStr;
		}
		int year = paymentDate.getPayStartDate().getYear() + 1900;
		endMDStr = year + "/" + endMDStr;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			return format.parse(endMDStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Map<String, HpsHeatingShenfenXingzhiPreferential> shenfenPrefersMap
			= new HashMap<String, HpsHeatingShenfenXingzhiPreferential>();
	
	// 导入取暖费身份性质优惠
	private void importHpsHeatingShefenXingzhiPreferentail() {
		String qDatePreferStr = "SELECT TOP 1000 [heatingPreferentialsID]"
				+ "      ,[paymentMainID]"
				+ "      ,[preferentiaNeedsStatusName]"
				+ "      ,[preferentialTitle]"
				+ "      ,[preferentialValue]"
				+ "      ,[preferentialRate]"
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[heatingPreferentials] where preferentiaNeedsStatusName != '一般'";
		SQLQuery qDatePrefer = sessionWeb.createSQLQuery(qDatePreferStr);
		List<Object[]> preferAttrArrayList = qDatePrefer.list();
		for (Object[] preferAttrArray : preferAttrArrayList) {
			Integer paymentMainId = (Integer) preferAttrArray[1];
			HpsHeatingPaymentDate paymentDate = hpsHeatingPaymentDateMap.get(paymentMainId);
			if (paymentDate == null) {
				throw new RuntimeException("can't find heatingPaymentMain id:" + paymentMainId);
			}
			String statusName = (String) preferAttrArray[2];
			String title = (String) preferAttrArray[3];
			BigDecimal offRate = (BigDecimal) preferAttrArray[5];
			String remarks = (String) preferAttrArray[6];
			
			HpsHeatingShenfenXingzhiPreferential prefer = new HpsHeatingShenfenXingzhiPreferential();
			HpsDictItem shenfenXingzhiItem = shenfenXingzhiNameMap.get(statusName);
			if (shenfenXingzhiItem == null) {
				throw new RuntimeException("shenfengXingzhiItem statusName: " + statusName + " not existent");
			}
			prefer.setShenfengXingzhi(shenfenXingzhiItem);
			prefer.setPaymentDate(paymentDate);
			prefer.setRemarks(remarks);
			BigDecimal payRate = new BigDecimal("1").subtract(offRate);
			prefer.setPayRate(payRate.doubleValue());
			prefer.setTitle(title);
			session.save(prefer);
			shenfenPrefersMap.put(paymentMainId + "_" + title, prefer);
		}
	}
	
	private Map<String, HpsHeatingUnit> unitsMap = new HashMap<String, HpsHeatingUnit>();
	
	// 导入取暖费单价
	private void importHpsHeatingUnit() {
		String sUnitStr = "SELECT TOP 1000 [heatingPricesID]"
				+ "      ,[paymentMainID]"
				+ "      ,[priceTitle]"
				+ "      ,[priceValue]"
				+ "      ,[isCommercial]"
				+ "      ,[wasteHeatCollectRate]" // 简单起见，没有设定这个值，直接硬编码20%
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[heatingPrices]";
		SQLQuery qUnit = sessionWeb.createSQLQuery(sUnitStr);
		List<Object[]> unitAttrArrayList = qUnit.list();
		for (Object[] unitAttrArray : unitAttrArrayList ) {
			Integer paymentMainId = (Integer) unitAttrArray[1];
			String title = (String) unitAttrArray[2];
			BigDecimal unitBigDecimal = (BigDecimal) unitAttrArray[3];
			Boolean isCommercial = (Boolean) unitAttrArray[4];
			HpsDictItem yongfangXingzhiItem = null;
			if (isCommercial) {
				yongfangXingzhiItem = commercialItem;
			} else {
				yongfangXingzhiItem = personalItem;
			}
			String remarks = (String) unitAttrArray[6];
			HpsHeatingUnit unit = new HpsHeatingUnit();
			HpsHeatingPaymentDate paymentDate = hpsHeatingPaymentDateMap.get(paymentMainId);
			if (paymentDate == null) {
				throw new RuntimeException("can't find heatingPaymentMain id:" + paymentMainId);
			}
			unit.setPaymentDate(paymentDate);
			unit.setUnit(unitBigDecimal.doubleValue());
			unit.setRemarks(remarks);
			unit.setYongfangXingzhi(yongfangXingzhiItem);
			session.save(unit);
			unitsMap.put(paymentMainId + "_" + title, unit);
		}
	}
	
	private Map<String, HpsUser> hpsUsersMap = new HashMap<String, HpsUser>();
	
	private void importHpsUsers() {
		Role roleAdmin = (Role) session.load(Role.class, 1l);
		Role roleSuperAdmin = (Role) session.load(Role.class, 3l);
		Role roleOper = (Role) session.load(Role.class, 2l);
		String qUsersStr = "SELECT TOP 1000 [operatorID]" // 0
				+ "      ,[operatorName]"
				+ "      ,[roleName]" // 2
				+ "      ,[stationID]" // 3
				+ "      ,[uid]"// 4
				+ "      ,[pwd]" // 5
				+ "      ,[sex]" // 6
				+ "      ,[phone]" // 7
				+ "      ,[frozen]"
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[operator]";
		SQLQuery qusers = sessionWeb.createSQLQuery(qUsersStr);
		List<Object[]> userAttrArrayList = qusers.list();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		for (Object[] userAttrArray : userAttrArrayList) {
			String operName = (String) userAttrArray[1];
			String roleName = (String) userAttrArray[2];
			Integer stationid = (Integer) userAttrArray[3];
			String accountName = (String) userAttrArray[4];
			String psw = (String) userAttrArray[5];
			String sexStr = (String) userAttrArray[6];
			String phoneNo = (String) userAttrArray[7];
			HpsBase base = hpsBaseMap.get(stationid);
			if (base == null) {
				throw new RuntimeException("can't find base : " + stationid);
			}
			HpsUser user = new HpsUser();
			User appUser = new User();
			appUser.setEnabled(true);
			user.setUser(appUser);
			user.setBase(base);
			if (roleName.equals("超级管理员")) {
				user.getUser().addRole(roleSuperAdmin);
				user.setType(HpsUserType.ROLE_SUPERADMIN);
			} else if (roleName.equals("管理员")) {
				user.getUser().addRole(roleAdmin);
				user.setType(HpsUserType.ROLE_ADMIN);
			} else { // 操作员
				user.getUser().addRole(roleOper);
				user.setType(HpsUserType.ROLE_USER);
			}
			user.setMobilePhoneNo(phoneNo);
			user.setSex(sexStr.equals("男") ? Sex.MALE : Sex.FEMALE);
			user.setUserName(operName);
			user.setAccountName(accountName);
			user.setPassword(passwordEncoder.encode(psw));
			session.save(user);
			hpsUsersMap.put(operName, user);
		}
	}
	
	// 导入取暖费缴费记录(不包括上线之前历年结转的数据)
	private void importHpsHeatingChargeRecord() {
		String qChargeRecordStr = "SELECT h.[heatingPaymentID] a" // 0
				+ "      ,h.[heatingPaymentID]" // 1
				+ "      ,[paymentMainID]" // 2
				+ "      ,[householdID]" // 3
				+ "      ,[areaName]" // 4
				+ "      ,[BSRName]" // 5
				+ "      ,[unitNum]" // 6
				+ "      ,[floorNum]" // 7
				+ "      ,[doorNum]" // 8
				+ "      ,[masterName]" // 9
				+ "      ,[sex]" // 10
				+ "      ,[phone]" // 11
				+ "      ,[statusName]" // 12
				+ "      ,[wageNum]" // 13
				+ "      ,[houseRemarks]" // 14
				+ "      ,[isCommercial]" // 15
				+ "      ,[stopHeating]" // 16
				+ "      ,[sharerName]" // 17
				+ "      ,[priceTitle]" // 18
				+ "      ,[priceValue]" // 19
				+ "      ,[wasteHeatCollectRate]" // 20
				+ "      ,[heatingAcreage]" // 21
				+ "      ,[preferentialTitle]" // 22
				+ "      ,[preferentiaNeedsStatusName]" // 23
				+ "      ,[preferentialValue]" // 24
				+ "      ,[preferentialRate]" // 25
				+ "      ,[factCollectPreferential]" // 26
				+ "      ,[expiredPayTitle]" // 27
				+ "      ,[expiredPayRate]" // 28
				+ "      ,[planCollectHeating]" // 29
				+ "      ,[collectFinished]" // 30
				+ "      ,d.expiredDays" // 31
				+ "      ,d.factCollectExpired" // 32
				+ "      ,d.factCollectHeating" // 33
				+ "      ,d.issuedInvoice" // 34
				+ "      ,d.operatorName" // 35
				+ "      ,d.payDateTime" // 36
				+ "      ,d.remarks" // 37
				+ "    FROM [commonDB].[dbo].[heatingPayment] h left outer join"
				+ "        [commonDB].[dbo].[heatingPaymentDetail] d  "
				+ "    on"
				+ "        h.heatingPaymentID = d.heatingPaymentID";
		SQLQuery qChargeRecord = sessionWeb.createSQLQuery(qChargeRecordStr);
		List<Object[]> recordAttrArrayList = qChargeRecord.list();
		for (Object[] recordAttrArray : recordAttrArrayList) {
			Integer paymentMainID = (Integer) recordAttrArray[2];
			Integer householdID = (Integer) recordAttrArray[3];
			HpsHouse house = hpsHouseMap.get(householdID);
			if (house == null) {
				throw new RuntimeException("can't find house householdID:" + householdID);
			}
			HpsHouseOwner houseOwner = house.getOwner();
			if (houseOwner == null) {
				throw new RuntimeException("can't find house owner householdID:" + householdID);
			}
			String masterName = (String) recordAttrArray[9];
			String remarks = "";
//			if (remarks != null && remarks.length() > 0) {
//				remarks += " ";
//			} else {
//				remarks = "";
//			}
			boolean abnormal = false;
			if (!masterName.equals(houseOwner.getName())) {
				// 姓名与房屋登记是记录的姓名不相等
				remarks += " 姓名不相等[" + masterName + "," + houseOwner.getName() + "]";
				abnormal = true;
			}
			String wageNum = (String) recordAttrArray[13];
			if (!wageNum.equals(houseOwner.getWageNum())) {
				remarks += " " + masterName + " 工资号不相等[" 
							+ wageNum + "," + houseOwner.getWageNum() + "]";
				abnormal = true;
			}
			String statusName = (String) recordAttrArray[12];
			String statusNameInHouse = house.getShenfenXingzhi().getName();
			if (!statusName.equals(statusNameInHouse)) {
				remarks += " " + masterName +  " 身份性质不相等[" 
						+ statusName + "," + statusNameInHouse + "]";
				abnormal = true;
			}
			if (abnormal) {
				remarks = "房主信息异常：[地址：" + house.getAddress() + "]>>>>>>>" + remarks + "<<<<<<<";
				System.out.println(remarks);
			}
			String houseRemarks = (String) recordAttrArray[14];
			if (StringUtils.isNotEmpty(houseRemarks)) {
				remarks += " 原系统缴费时记录住房备注：" + houseRemarks;
			}
			HpsHeatingChargeRecord record = new HpsHeatingChargeRecord();
			record.setHouse(house);
			record.setHouseOwner(houseOwner);
			String preferTitle = (String) recordAttrArray[22];
			if (preferTitle != null && preferTitle.length() > 0) {
				// 身份性质优惠
				HpsHeatingShenfenXingzhiPreferential sfPrefer = shenfenPrefersMap
						.get(paymentMainID + "_" + preferTitle);
				if (sfPrefer != null) {
					record.setShenfenXingzhiPreferential(sfPrefer);
				} else {
					// 缴纳日期优惠
					HpsHeatingPaymentDatePreferential datePrefer = datePrefersMap
							.get(paymentMainID + "_" + preferTitle);
					record.setPaymentDatePreferential(datePrefer);
					if (datePrefer == null) {
						throw new RuntimeException("没有找到优惠:" + preferTitle);
					}
				}
			}
			// 单价
			String priceTitle = (String) recordAttrArray[18];
			HpsHeatingUnit unit = unitsMap.get(paymentMainID + "_" + priceTitle);
			if (unit == null) {
				throw new RuntimeException("没有找到单价:" + priceTitle);
			}
			record.setUnit(unit);
			Boolean stopped = (Boolean)  recordAttrArray[16];
			record.setStopped(stopped);
			// 实收
			BigDecimal actualCharged = (BigDecimal) recordAttrArray[33];
			if (actualCharged == null) {
				actualCharged = new BigDecimal("0");
			}
			// 标收
			BigDecimal normalCharged = (BigDecimal) recordAttrArray[29];
			record.setActualSumCharge(actualCharged.doubleValue());
			record.setNormalHeatingCharge(normalCharged.doubleValue());
			Integer expiredDays = (Integer) recordAttrArray[31];
			if (expiredDays == null) {
				expiredDays = 0;
			}
			record.setExpiredDays(expiredDays);
			Boolean finished = (Boolean) recordAttrArray[30];
			if (finished) {
				record.setChargeState(ChargeStateEnum.CHARGED);
			} else {
				record.setChargeState(ChargeStateEnum.UNCHARGED);
			}
//			Booean issuedInvoice
			Date payDateTime = (Date) recordAttrArray[36];
			record.setChargeDate(payDateTime);
			String recordRemarks = (String) recordAttrArray[37];
			if (StringUtils.isNotEmpty(recordRemarks)) {
				remarks += " 原系统缴费备注：" + recordRemarks;
			}
			record.setRemarks(remarks);
			String operName = (String) recordAttrArray[35];
			if (StringUtils.isNotEmpty(operName)) {
				HpsUser user = hpsUsersMap.get(operName);
				if (user == null) {
					throw new RuntimeException("没有找到用户：" + operName);
				}
				record.setOperUser(user);
			}
			BigDecimal prefer = (BigDecimal) recordAttrArray[26];
			record.setPreferential(prefer.doubleValue());
			double mustHeatingCharge = normalCharged.subtract(prefer).doubleValue();
			record.setMustHeatingCharge(mustHeatingCharge);
			record.setMustSumCharge(mustHeatingCharge);
			record.setMustZhinajin(0d);
			record.setDivertedCharge(0d);
			record.setWageNum(wageNum);
			record.setPaymentDate(hpsHeatingPaymentDateMap.get(paymentMainID));
			session.save(record);
		}
	}
	
	private Map<Integer, HpsMaintainPaymentDate> maintainPaymentDateMap 
				= new HashMap<Integer, HpsMaintainPaymentDate>();
	
	private void importMaintainPaymentDate() {
		String qPaymentDateStr = "SELECT TOP 1000 [paymentMainID]" // 0
				+ "      ,[stationID]" // 1
				+ "      ,[paymentTitle]" // 2
				+ "      ,[payStart]" // 3
				+ "      ,[payStop]" // 4
				+ "      ,[diverted]" // 5
				+ "      ,[remarks]" // 6
				+ "  FROM [commonDB].[dbo].[houseMaintainPaymentMain]";
		SQLQuery qPaymentDate = sessionWeb.createSQLQuery(qPaymentDateStr);
		List<Object[]> dateAttrArrayList = qPaymentDate.list();
		for (Object[] itemAttrArray : dateAttrArrayList) {
			Integer paymentMainId = (Integer) itemAttrArray[0];
			Integer stationId = (Integer) itemAttrArray[1];
			String title = (String) itemAttrArray[2];
			Date payStart = (Date) itemAttrArray[3];
			Date payEnd = (Date) itemAttrArray[4];
			String remarks = (String) itemAttrArray[6];
			HpsMaintainPaymentDate paymentDate = new HpsMaintainPaymentDate();
			HpsBase base = hpsBaseMap.get(stationId);
			if (base == null) {
				throw new RuntimeException("can't find base : " + stationId);
			}
			paymentDate.setBase(base);
			paymentDate.setChargeRecordsInitialized(true);
			paymentDate.setPayStartDate(payStart);
			paymentDate.setPayEndDate(payEnd);
			paymentDate.setRemarks(remarks);
			paymentDate.setTitle(title);
			session.save(paymentDate);
			maintainPaymentDateMap.put(paymentMainId, paymentDate);
		}
	}
	
	private Map<String, HpsMaintainUnit> maintainUnitsMap = new HashMap<String, HpsMaintainUnit>();
	
	private void importMaintainUnit() {
		String sUnitStr = "SELECT TOP 1000 [houseMaintainPricesID]"
				+ "      ,[paymentMainID]"
				+ "      ,[priceTitle]"
				+ "      ,[priceValue]"
				// 由于老系统新旧房都是一个单价，而且所有房屋中也没有旧房，我们的系统废除这个字段
				// 是按照用房性质来设置单价
				+ "      ,[isOldHouse]" 
				+ "      ,[remarks]"
				+ "  FROM [commonDB].[dbo].[houseMaintainPrices]";
		SQLQuery qUnit = sessionWeb.createSQLQuery(sUnitStr);
		List<Object[]> unitAttrArrayList = qUnit.list();
		for (Object[] unitAttrArray : unitAttrArrayList ) {
			Integer paymentMainId = (Integer) unitAttrArray[1];
			String title = (String) unitAttrArray[2];
			BigDecimal unitBigDecimal = (BigDecimal) unitAttrArray[3];
			Boolean isCommercial = (Boolean) unitAttrArray[4];
			HpsDictItem yongfangXingzhiItem = null;
			if (isCommercial) {// 旧房=>商用，费旧房=>个人，这个没什么特殊用意，只是想让每一种用房性质都对应一个单价
				yongfangXingzhiItem = commercialItem;
			} else {
				yongfangXingzhiItem = personalItem;
			}
			String remarks = (String) unitAttrArray[5];
			HpsMaintainUnit unit = new HpsMaintainUnit();
			HpsMaintainPaymentDate paymentDate = maintainPaymentDateMap.get(paymentMainId);
			if (paymentDate == null) {
				throw new RuntimeException("can't find heatingPaymentMain id:" + paymentMainId);
			}
			unit.setPaymentDate(paymentDate);
			unit.setUnit(unitBigDecimal.doubleValue());
			unit.setRemarks(remarks);
			unit.setYongfangXingzhi(yongfangXingzhiItem);
			session.save(unit);
			maintainUnitsMap.put(paymentMainId + "_" + title, unit);
		}
	}
	
	// 导入维修费缴费记录，不包括上线之前历年结转数据
	private void importMaintainChargeRecord() {
		String qChargeRecordStr = "SELECT h.[houseMaintainPaymentID]" // 0
				+ "      ,[paymentMainID]" // 1
				+ "      ,[householdID]" // 2
				+ "      ,[areaName]" // 3
				+ "      ,[BSRName]" // 4
				+ "      ,[unitNum]" // 5
				+ "      ,[floorNum]" // 6
				+ "      ,[doorNum]" // 7
				+ "      ,[masterName]" // 8
				+ "      ,[sex]" // 9
				+ "      ,[phone]" // 10
				+ "      ,[statusName]" // 11
				+ "      ,[wageNum]" // 12
				+ "      ,[houseRemarks]" // 13
				+ "      ,[isOldHouse]" // 14
				+ "      ,[priceTitle]" // 15
				+ "      ,[priceValue]" // 16
				+ "      ,[houseMaintainAcreage]" // 17
				+ "      ,[planCollectHouseMaintain]" // 18
				+ "      ,[collectFinished]" // 19
				+ "      ,d.factCollectHouseMaintain" // 20
				+ "      ,d.issuedInvoice" // 21
				+ "      ,d.operatorName" // 22
				+ "      ,d.payDateTime"// 23
				+ "      ,d.remarks"// 24
				+ "  FROM [commonDB].[dbo].[houseMaintainPayment] h left outer join"
				+ "   [commonDB].[dbo].houseMaintainPaymentDetail d on"
				+ "   h.houseMaintainPaymentID = d.houseMaintainPaymentID";
		SQLQuery qChargeRecord = sessionWeb.createSQLQuery(qChargeRecordStr);
		List<Object[]> recordAttrArrayList = qChargeRecord.list();
		for (Object[] recordAttrArray : recordAttrArrayList) {
			Integer paymentMainID = (Integer) recordAttrArray[1];
			Integer householdID = (Integer) recordAttrArray[2];
			HpsHouse house = hpsHouseMap.get(householdID);
			if (house == null) {
				throw new RuntimeException("can't find house householdID:" + householdID);
			}
			HpsHouseOwner houseOwner = house.getOwner();
			if (houseOwner == null) {
				throw new RuntimeException("can't find house owner householdID:" + householdID);
			}
			String masterName = (String) recordAttrArray[8];
			String remarks = "";
			boolean abnormal = false;
			if (!masterName.equals(houseOwner.getName())) {
				// 姓名与房屋登记是记录的姓名不相等
				remarks += " 姓名不相等[" + masterName + "," + houseOwner.getName() + "]";
				abnormal = true;
			}
			String wageNum = (String) recordAttrArray[12];
			if (!wageNum.equals(houseOwner.getWageNum())) {
				remarks += " " + masterName + " 工资号不相等[" 
							+ wageNum + "," + houseOwner.getWageNum() + "]";
				abnormal = true;
			}
			if (abnormal) {
				remarks = "房主信息异常：[地址：" + house.getAddress() + "]>>>>>>>" + remarks + "<<<<<<<";
				System.out.println(remarks);
			}
			String houseRemarks = (String) recordAttrArray[13];
			if (StringUtils.isNotEmpty(houseRemarks)) {
				remarks += " 原系统缴费时记录住房备注：" + houseRemarks;
			}
			HpsMaintainChargeRecord record = new HpsMaintainChargeRecord();
			record.setHouse(house);
			record.setHouseOwner(houseOwner);
			// 单价
			String priceTitle = (String) recordAttrArray[15];
			HpsMaintainUnit unit = maintainUnitsMap.get(paymentMainID + "_" + priceTitle);
			if (unit == null) {
				throw new RuntimeException("没有找到单价:" + priceTitle);
			}
			record.setUnit(unit);
			// 应收
			BigDecimal mustCharge = (BigDecimal) recordAttrArray[18];
			if (mustCharge == null) {
				mustCharge = new BigDecimal("0");
			}
			record.setMustCharge(mustCharge.doubleValue());
			// 实收
			BigDecimal actualCharged = (BigDecimal) recordAttrArray[20];
			if (actualCharged == null) {
				actualCharged = new BigDecimal("0");
			}
			record.setActualCharge(actualCharged.doubleValue());
			Boolean finished = (Boolean) recordAttrArray[19];
			if (finished) {
				record.setChargeState(ChargeStateEnum.CHARGED);
			} else {
				record.setChargeState(ChargeStateEnum.UNCHARGED);
			}
			Date payDateTime = (Date) recordAttrArray[23];
			record.setChargeDate(payDateTime);
			String recordRemarks = (String) recordAttrArray[24];
			if (StringUtils.isNotEmpty(recordRemarks)) {
				remarks += " 原系统缴费备注：" + recordRemarks;
			}
			record.setRemarks(remarks);
			String operName = (String) recordAttrArray[22];
			if (StringUtils.isNotEmpty(operName)) {
				HpsUser user = hpsUsersMap.get(operName);
				if (user == null) {
					throw new RuntimeException("没有找到用户：" + operName);
				}
				record.setOperUser(user);
			}
			record.setDivertedCharge(0d);
			record.setWageNum(wageNum);
			record.setMonthCount(12);
			record.setPaymentDate(maintainPaymentDateMap.get(paymentMainID));
			session.save(record);
		}
	}
	
	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			hpsObject.setCreateTime(currentDate);
			hpsObject.setCreateUsetId(-1l);
			hpsObject.setCreateUserName("OLD_SYS");
		}
		return false;
	}
	
	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			hpsObject.setLastUpdateTime(currentDate);
			hpsObject.setLastUpdateUserId(-1l);
			hpsObject.setLastUpdateUserName("OLD_SYS");
		}
		return false;
	}
	

}
