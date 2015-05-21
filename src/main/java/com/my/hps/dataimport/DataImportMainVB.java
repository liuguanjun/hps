package com.my.hps.dataimport;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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

import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsDictItem;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricChargeRecord;
import com.my.hps.webapp.model.HpsElectricPaymentDate;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.HpsHouse;
import com.my.hps.webapp.model.HpsHouseOwner;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.HpsUserType;
import com.my.hps.webapp.model.enums.Sex;

public class DataImportMainVB implements PreInsertEventListener, PreUpdateEventListener {
	
	private Session sessionVB;
	private Session session;
	private Date currentDate = new Date();
	
	private HpsDictItem personalItem;
	private HpsDictItem commercialItem;
	
//	private List<HpsBase> hpsBaseList = new ArrayList<HpsBase>();
	private Map<Integer, HpsArea> areaMap = new HashMap<Integer, HpsArea>();
	
	private Map<Integer, HpsHouse> houseMap = new HashMap<Integer, HpsHouse>();
	
	public static void main(String[] args) throws Exception {
		DataImportMainVB main = new DataImportMainVB();
		main.setSession();
		main.clearData();
		main.importHpsData();
		main.releaseSession();
	}
	
	// 用房性质
	private void prepareYongfangXingzhiData() {
		// 个人
		personalItem = (HpsDictItem) session.get(HpsDictItem.class, 6l);
		// 商用
		commercialItem = (HpsDictItem) session.get(HpsDictItem.class, 7l);
	}
	
	private void prepareAreaList() {
		String sqlArea = "from HpsArea where base.id = 1";
		List<HpsArea> hpsAreaList = session.createQuery(sqlArea).list();
		Set<String> areaNameSet = new HashSet<String>();
		for (HpsArea area : hpsAreaList) {
			areaNameSet.add(area.getName());
		}
		// 校验一下当前数据库和老系统中的Area是否完全一致，如果不一致抛出异常
		String sqlAreaVB = "SELECT [areaID]"
				+ "      ,[areaName]"
				+ "  FROM [ZSYJ].[dbo].[area]";
		SQLQuery qAreaVB = sessionVB.createSQLQuery(sqlAreaVB);
		@SuppressWarnings("unchecked")
		List<Object[]> itemAttrArrayList = qAreaVB.list();
		Set<String> areaNameSetVB = new HashSet<String>();
		for (Object[] itemAttrArray : itemAttrArrayList) {
			String areaNameVB = (String) itemAttrArray[1];
			areaNameSetVB.add(areaNameVB);
			boolean found = false;
			for (HpsArea area : hpsAreaList) {
				if (areaNameVB.equals(area.getName())) {
					areaMap.put((Integer) itemAttrArray[0], area);
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("没有发现旧系统的区域：" + areaNameVB);
			}
		}
		if (!areaNameSet.equals(areaNameSetVB)) {
			System.out.println("===========新老系统的Area不一致============");
			System.out.println("老系统的Area:" + areaNameSetVB);
			System.out.println("新系统的Area:" + areaNameSet);
			//throw new RuntimeException("老系统的Area和锡系统的Area不匹配");
		}
	}
	
	private void setSession() {
		// 新建系统
		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.1.xml")
				.buildSessionFactory();
		session = sessionFactory.openSession();
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(
        EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(this);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(this);
		// 老系统(VB)
		SessionFactory sessionFactoryVB = new Configuration().configure("/hibernate.cfg.vb.xml")
				.buildSessionFactory();
		sessionVB = sessionFactoryVB.openSession();
		// 老系统(Web)
	}
	
	private void releaseSession() {
		session.close();
		sessionVB.close();
	}
	
	private void clearData() {
		//session.createSQLQuery("delete from hps_maintain_charge_record").executeUpdate();
	}
	
	private void importHpsData() throws Exception {
		Transaction t = session.beginTransaction();
		prepareYongfangXingzhiData();
		prepareAreaList();
		importHpsElectricUnit();
		checkUsers();
		checkHouseOwners();
		checkHouse();
		importHpsElectricPaymentDate();
		importHpsElectricChargeRecord();
		t.rollback();
		System.out.println("------------------OK-----------------");
	}
	
	private Map<String, HpsElectricUnit> electricUnitMap = new HashMap<String, HpsElectricUnit>();
	
	// 此处需要和客户确认1.040商用电价和0.525商用电价是怎么回事儿
	private void importHpsElectricUnit() {
		session.createQuery("delete from HpsElectricChaobiao").executeUpdate();
		session.createQuery("delete from HpsElectricChargeRecord").executeUpdate();
		session.createSQLQuery("delete from hps_electric_unit").executeUpdate();
		String qUnitStr = "SELECT a.areaName"
				+ "      ,h.isBusiness"
				+ "      ,p.electricityPrice"
				+ "      ,b.storiedBuildingID"
				+ "      ,MAX(p.createDateTime)"
				+ "  FROM [ZSYJ].[dbo].[storiedBuilding] b, [ZSYJ].[dbo].area a, [ZSYJ].[dbo].house h"
				+ "  , [ZSYJ].[dbo].[electricityPayment] p"
				+ "  where b.areaID = a.areaID"
				+ "  and b.storiedBuildingID = h.storiedBuildingID"
				+ "  and h.houseID = p.houseID"
				+ "  and p.electricityPrice != 1.040" // 之前的电价,目前已经不适用
				+ "  and (h.isBusiness = 0 or p.electricityPrice != 0.525)" // 之前的电价,目前已经不适用
				+ "  group by areaName, isBusiness, electricityPrice, b.storiedBuildingID"
				+ "  order by storiedBuildingID, areaName, isBusiness, electricityPrice";
		SQLQuery qUnit = sessionVB.createSQLQuery(qUnitStr);
		@SuppressWarnings("unchecked")
		List<Object[]> itemAttrArrayList = qUnit.list();
		for (Object[] itemAttrArray : itemAttrArrayList) {
			String areaName = (String) itemAttrArray[0];
			Boolean isCommercial = (Boolean) itemAttrArray[1];
			HpsDictItem yongfangXingzhiItem = null;
			if (isCommercial) {
				yongfangXingzhiItem = commercialItem;
			} else {
				yongfangXingzhiItem = personalItem;
			}
			BigDecimal unit = (BigDecimal) itemAttrArray[2];
			Integer louzuoId = (Integer) itemAttrArray[3];
		}
		// 通过以上SQL得出，目前口前的电价为商用1.080，个人是0.525
		// 对于除此之外的电价，手动去电价设置页面修改
		HpsElectricUnit bizzUnit = new HpsElectricUnit();
		bizzUnit.setBase((HpsBase)session.load(HpsBase.class, 1l));
		bizzUnit.setYongfangXingzhi(commercialItem);
		bizzUnit.setLevel(false);
		bizzUnit.setHistory(false);
		bizzUnit.setUnit(1.080d);
		bizzUnit.setZhinaScale(0.003);
		bizzUnit.setWeishengfei(2.0d);// 卫生费
		electricUnitMap.put(bizzUnit.getUnit() + "-" + bizzUnit.getWeishengfei(), bizzUnit);
		session.save(bizzUnit);
		
		HpsElectricUnit bizzUnit2 = new HpsElectricUnit();
		bizzUnit2.setBase((HpsBase)session.load(HpsBase.class, 1l));
		bizzUnit2.setYongfangXingzhi(commercialItem);
		bizzUnit2.setLevel(false);
		bizzUnit2.setHistory(true);
		bizzUnit2.setZhinaScale(0.003);
		bizzUnit2.setUnit(1.080d);
		bizzUnit2.setWeishengfei(0d);// 卫生费
		electricUnitMap.put(bizzUnit2.getUnit() + "-" + bizzUnit2.getWeishengfei(), bizzUnit2);
		session.save(bizzUnit2);
		
		HpsElectricUnit personUnit = new HpsElectricUnit();
		personUnit.setBase((HpsBase)session.load(HpsBase.class, 1l));
		personUnit.setYongfangXingzhi(personalItem);
		personUnit.setLevel(false);
		personUnit.setHistory(false);
		personUnit.setUnit(0.525d);
		personUnit.setWeishengfei(2.0d);// 卫生费
		personUnit.setZhinaScale(0.003);
		electricUnitMap.put(personUnit.getUnit() + "-" + personUnit.getWeishengfei(), personUnit);
		session.save(personUnit);
		
		HpsElectricUnit personUnit2 = new HpsElectricUnit();
		personUnit2.setBase((HpsBase)session.load(HpsBase.class, 1l));
		personUnit2.setYongfangXingzhi(personalItem);
		personUnit2.setLevel(false);
		personUnit2.setHistory(true);
		personUnit2.setUnit(0.525d);
		personUnit2.setZhinaScale(0.003);
		personUnit2.setWeishengfei(0d);// 卫生费
		electricUnitMap.put(personUnit2.getUnit() + "-" + personUnit2.getWeishengfei(), personUnit2);
		session.save(personUnit2);
		
		// 本次导入不包括旅顺的电费数据，所以旅顺的电费单价等数据，手工录入
	}
	
	private Map<Date, HpsElectricPaymentDate> paymentDateMap = new HashMap<Date, HpsElectricPaymentDate>();
	
	private void importHpsElectricPaymentDate() throws Exception {
		session.createQuery("delete from HpsElectricPaymentDate").executeUpdate();
		Calendar currentCalendar = new GregorianCalendar();
		int currentMonthInt = currentCalendar.get(Calendar.YEAR) * 100 +
				currentCalendar.get(Calendar.MONTH);
		int startMonthInt = 201401;
		DateFormat formatYM = new SimpleDateFormat("yyyyMM");
		DateFormat formatYMD = new SimpleDateFormat("yyyyMMdd");
		HpsBase kouqianBase = (HpsBase) session.load(HpsBase.class, 1l);
		while (true) {
			HpsElectricPaymentDate paymentDate = new HpsElectricPaymentDate();
			paymentDate.setBase(kouqianBase);
			Date month = new java.sql.Date(formatYM.parse(startMonthInt + "").getTime());
			paymentDate.setMonth((java.sql.Date) month);
			paymentDate.setStartDate(new java.sql.Date(formatYMD.parse((startMonthInt + 1) + "20").getTime()));
			paymentDate.setEndDate(new java.sql.Date(formatYMD.parse((startMonthInt + 2) + "05").getTime()));
			if (startMonthInt <= 201408) {
				paymentDate.setChaobiaosInitialized(true);
			}
			session.save(paymentDate);
			paymentDateMap.put(month, paymentDate);
			if (startMonthInt == currentMonthInt) {
				break;
			}
			startMonthInt++;
		}
	}
	
	private Map<Integer, String> oldHouseAddressMap = new HashMap<Integer, String>();
	
	private void checkHouse() {
		String qHouseVBStr = "SELECT [houseID]" // 0
				+ "      ,h.[storiedBuildingID]" // 1
				+ "      ,[localUnitNum]" // 2
				+ "      ,[floorNum]" // 3
				+ "      ,[localDoorNum]" // 4
				+ "      ,b.localNum" // 5
				+ "      ,[orderNum]" // 6
				+ "      ,b.areaId" // 7
				+ "      ,[heatingArea]"// 8
				+ "      ,[enjoyPreferentialArea]" // 9
				+ "      ,[maintainArea]" // 10
				+ "      ,[InstrumentationNumOEM]" // 11
				+ "      ,[InstrumentationNumCUM]" // 12
				+ "      ,[hasHeating]" // 13
				+ "      ,[hasHouseMaintain]" // 14
				+ "      ,[hasElectric]" // 15
				+ "      ,[hasElectricMaintain]" // 16
				+ "      ,[hasCorridorLight]" // 17
				+ "      ,[hasClean]" // 18
				+ "      ,[isBusiness]" // 19
				+ "      ,[createDate]" // 20
				+ "      ,a.areaName + '-' + b.localNum + '号楼-' + CONVERT(varchar(3), h.localUnitNum) + '单元-' + CONVERT(varchar(3), h.floorNum)"
                + " + '层' + ',@' + CONVERT(varchar(3), h.orderNum) address" // 21
				+ "      ,a.areaName " // 22
				+ "  FROM [ZSYJ].[dbo].[house] h, [ZSYJ].[dbo].storiedBuilding b, [ZSYJ].[dbo].area a "
				+ "  where h.storiedBuildingID = b.storiedBuildingID and orderNum is not null"
				+ "  and a.areaID = b.areaID";
		SQLQuery qHouseVB = sessionVB.createSQLQuery(qHouseVBStr);
		@SuppressWarnings("unchecked")
		List<Object[]> houseAttrArrayList = qHouseVB.list();
		@SuppressWarnings("unchecked")
		List<HpsHouse> houseList = session.createQuery("from HpsHouse where louzuo.area.base.id = 1").list();
		System.out.println("============check房屋开始===============");
		System.out.println("新系统房屋在老系统没有找到:");
		int i = 1;
		for (HpsHouse house : houseList) {
			String louzuoName = house.getLouzuo().getName();
			String doorNo = house.getDoorNo();
			String danyuan = house.getDanyuan();
			String ceng = house.getCeng();
			String areaName = house.getLouzuo().getArea().getName();
			boolean found = false;
			for (Object[] houseAttrArray : houseAttrArrayList) {
				String luozuoNameVB = (String) houseAttrArray[5];
				String danyuanVB = houseAttrArray[2].toString();
				String doorNoVB = houseAttrArray[6].toString();
				String cengVB = houseAttrArray[3].toString();
				Integer areaId = (Integer) houseAttrArray[7];
				String areaNameVB = "";
				try {
					areaNameVB = areaMap.get(areaId).getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (louzuoName.trim().equals(luozuoNameVB.trim())
						&& doorNo.trim().equals(doorNoVB.trim()) && danyuan.trim().equals(danyuanVB.trim())
						&& ceng.trim().equals(cengVB.trim()) && areaName.trim().equals(areaNameVB.trim())) {
					found = true;
					houseMap.put((Integer)houseAttrArray[0], house);
					break;
				}
			}
			if (!found) {
				System.out.println(i++ + ":" + house.getAddress());
			}
		}
		//System.out.println("老系统房屋在新系统没有找到:");
		for (Object[] houseAttrArray : houseAttrArrayList) {
//			String luozuoNameVB = (String) houseAttrArray[5];
//			String danyuanVB = houseAttrArray[2].toString();
//			String doorNoVB = houseAttrArray[6].toString();
//			String cengVB = houseAttrArray[3].toString();
//			String areaNameVB = houseAttrArray[22].toString();
			Integer oldId = (Integer) houseAttrArray[0];
			String address = (String) houseAttrArray[21];
			oldHouseAddressMap.put(oldId, address);
//			boolean found = false;
//			for (HpsHouse house : houseList) {
//				String louzuoName = house.getLouzuo().getName();
//				String doorNo = house.getDoorNo();
//				String danyuan = house.getDanyuan();
//				String ceng = house.getCeng();
//				String areaName = house.getLouzuo().getArea().getName();
//				if (louzuoName.equals(luozuoNameVB)
//						&& doorNo.equals(doorNoVB) && danyuan.equals(danyuanVB)
//						&& ceng.equals(cengVB) && areaName.equals(areaNameVB)) {
//					found = true;
//					//houseMap.put((Integer)houseAttrArray[0], house);
//					break;
//				}
//			}
//			if (!found) {
//				System.out.println(i++ + ":" + houseAttrArray[21]);
//			}
		}
		System.out.println("============check房屋结束===============");
	}
	
	private void importHpsElectricChargeRecord() {
		System.out.println("============导入缴费记录开始===============");
		String qElectricRecordStr = "SELECT [electricityPaymentID]" // 0
				+ "      ,[houseID]"// 1
				+ "      ,[houseMasterID]"// 2
				+ "      ,[electricityPrice]"// 3
				+ "      ,[shangCiDianLiangSum]"// 4
				+ "      ,[instrumentationValue]"// 5
				+ "      ,[nowMonthDianValue]"// 6
				+ "      ,[nowMonthDianMoney]"// 7
				+ "      ,[shouldPayment]"// 8
				+ "      ,[factPayment]"// 9
				+ "      ,[paymentDateTime]"// 10
				+ "      ,[createDateTime]"// 11
				+ "      ,[operatorEmployeID]"// 12
				+ "      ,[cashierEmployeID]"// 13
				+ "      ,[readerEmployeID]"// 14
				+ "      ,[readDateTime]"// 15
				+ "      ,[maintainPrice]"// 16
				+ "      ,[cleanPrice]"// 17
				+ "      ,[corridorLightPrice]"// 18
				+ "      ,[balance]"// 19
				+ "      ,[overPaymentDate]"// 20
				+ "      ,[expiredDays]"// 21
				+ "      ,[calculate]"// 22
				+ "      ,[expiredMoney]"// 23
				+ "      ,[electricExpiredMoneyID]"// 24
				+ "  FROM [ZSYJ].[dbo].[electricityPayment]"
				+ "  where convert(varchar(4), createDateTime, 120) = '2014'"
				+ "  order by houseID, createDateTime";
		session.createQuery("delete from HpsElectricChaobiao").executeUpdate();
		session.createQuery("delete from HpsElectricChargeRecord").executeUpdate();
		HpsDictItem yichaobiaoItem = (HpsDictItem) session.createQuery("from HpsDictItem where code = 'YICHAOBIAO'")
				.uniqueResult();
		SQLQuery qElectricRecord = sessionVB.createSQLQuery(qElectricRecordStr);
		HpsBase kouqianBase = (HpsBase) session.load(HpsBase.class, 1l);
		List<Object[]> recordAttrArrayList = qElectricRecord.list();
		DateFormat formatYM = new SimpleDateFormat("yyyy-MM");
		Set<Integer> houseIdSet = new HashSet<Integer>();
		Set<Integer> ownerIdSet = new HashSet<Integer>();
		int i = 0;
		for (Object[] recordAttrArray : recordAttrArrayList) {
			Integer houseId = (Integer) recordAttrArray[1];
			Integer ownerId = (Integer) recordAttrArray[2];
			HpsHouse house = houseMap.get(houseId);
			if (house == null) {
				if (!houseIdSet.contains(houseId)) {
					System.out.println("没有发现旧系统的房屋[" + ++i + "]:houseoldid=" + houseId + "--->" + oldHouseAddressMap.get(houseId));
					houseIdSet.add(houseId);
				}
				continue;
			}
			HpsHouseOwner owner = houseOwnerMap.get(ownerId);
			if (owner == null) {
				owner = house.getOwner();
				if (!ownerIdSet.contains(ownerId)) {
					System.out.println("没有发现旧系统的房主[" + ++i + "]::owneroldid=" + ownerId + "--->" + oldHouseOwnerNameMap.get(ownerId));
					ownerIdSet.add(ownerId);
				}
				continue;
			}
			// 收费员
			Integer operId = (Integer) recordAttrArray[13];
			HpsUser operUser = userMap.get(operId);
			if (operId != null) {
				if (operUser == null) {
					System.out.println("没有发现旧用户:employeeId=" + operId);
					continue;
				}
			}
			Date overPaymentDate = (Date) recordAttrArray[20];
			Calendar overPaymentCalendar = Calendar.getInstance();
			overPaymentCalendar.setTime(overPaymentDate);
			int overMonth = overPaymentCalendar.get(Calendar.MONTH);
			overMonth -= 2;
			overPaymentCalendar.set(Calendar.MONDAY, overMonth);
			overPaymentCalendar.set(Calendar.DAY_OF_MONTH, 1);
			Date month = overPaymentCalendar.getTime();
			HpsElectricChaobiao chaobiao = new HpsElectricChaobiao();
			// 月份
//			chaobiao.setChargeMonth(formatYM.format(month));
			HpsElectricPaymentDate paymentDate = paymentDateMap.get(month);
			if (formatYM.format(month).equals("2013-12")) {
				continue;
			}
			if (paymentDate == null) {
				System.out.println("没有发现缴费日期定义:" + formatYM.format(month));
				continue;
			}
			chaobiao.setPaymentDate(paymentDate);
			// 单价
			BigDecimal unitVB = (BigDecimal) recordAttrArray[3];
			// 卫生费
			BigDecimal weishengVB = (BigDecimal) recordAttrArray[17];
			String key = unitVB.doubleValue() + "-" + weishengVB.doubleValue();
			HpsElectricUnit unit = electricUnitMap.get(key);
			if (unit == null) {
				System.out.println("没有发现旧单价:" + key);
				continue;
			}
			chaobiao.setUnit(unit);
//			chaobiao.setReadMeterFlag(yichaobiaoItem);
			// 上次表值
			BigDecimal provReadoutsElectric = (BigDecimal) recordAttrArray[4];
			// 本次表值
			BigDecimal readoutsElectric = (BigDecimal) recordAttrArray[5];
			// 用电量
			BigDecimal electricCount = (BigDecimal) recordAttrArray[6];
			// 电费
			BigDecimal electricCharge = (BigDecimal) recordAttrArray[7];
			// 滞纳金
			BigDecimal zhinajin = (BigDecimal) recordAttrArray[23];
			// 抄表日期
			Date readMeterDate = (Date) recordAttrArray[15];
			chaobiao.setProvReadoutsElectric(provReadoutsElectric.longValue());
			chaobiao.setReadoutsElectric(readoutsElectric.longValue());
			chaobiao.setElectricCount(electricCount.longValue());
			chaobiao.setElectricCharge(electricCharge.doubleValue());
			if (zhinajin != null) {
				chaobiao.setZhinajin(zhinajin.doubleValue());
			}
			chaobiao.setReadMeterDate(readMeterDate);
			chaobiao.setHouse(house);
			chaobiao.setWeishengCharge(weishengVB.doubleValue());
			chaobiao.setHouseOwner(owner);
			session.save(chaobiao);
			// 缴费日期
			Date paymenyDateVB = (Date) recordAttrArray[10];
			if (paymenyDateVB == null) { // 没有缴费
				continue;
			}
			HpsElectricChargeRecord chargeRecord = new HpsElectricChargeRecord();
			chargeRecord.setChargeDate(paymenyDateVB);
			chargeRecord.setHouse(house);
			chargeRecord.setChargeUser(operUser);
			// 上次余额
			BigDecimal fact = (BigDecimal) recordAttrArray[9];
			BigDecimal balance = (BigDecimal) recordAttrArray[19];
			BigDecimal should = (BigDecimal) recordAttrArray[8];
			// (实际缴纳 - 应该缴纳) + 上次余额 = balance
			// 上次余额 = balance - (实际缴纳 - 应该缴纳)
			double previousSurplus = 0d;
			try {
				previousSurplus = balance.subtract(fact.subtract(should)).doubleValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
			chargeRecord.setPreviousSurplus(previousSurplus);
			chargeRecord.setActualCharge(fact.doubleValue());
			chargeRecord.setCurrentSurplus(balance.doubleValue());
			chargeRecord.setMustCharge(should.doubleValue());
			chargeRecord.setCancelled(false);
			chargeRecord.setHouseOwner(owner);
			Set<HpsElectricChaobiao> chaobiaoSet = new HashSet<HpsElectricChaobiao>();
			chaobiaoSet.add(chaobiao);
			chaobiao.setChargeRecord(chargeRecord);
			chargeRecord.setChaobiaoSet(chaobiaoSet);
			session.save(chargeRecord);
		}
		
		
		
		System.out.println("============导入缴费记录结束===============");
	}
	
	private Map<Integer, HpsUser> userMap = new HashMap<Integer, HpsUser>();
	
	private void checkUsers() {
		session.createQuery("delete from HpsUser where createUserName like 'OLD_SYS_VB%'").executeUpdate();
		session.createQuery("delete from User where username like 'OLD_USER_%'").executeUpdate();
		String qUsersVBStr = "SELECT TOP 1000 [employeID]"
				+ "      ,[realName]"
				+ "      ,[sex]"
				+ "      ,[IDCard]"
				+ "      ,[contact]"
				+ "      ,[enable]"
				+ "  FROM [ZSYJ].[dbo].[employe] where enable = 1";
		SQLQuery qUsersVB = sessionVB.createSQLQuery(qUsersVBStr);
		@SuppressWarnings("unchecked")
		List<Object[]> userAttrArrayList = qUsersVB.list();
		List<HpsUser> userList = session.createQuery("from HpsUser where base.id = 1").list();
		int i = 1;
		System.out.println("============check用户开始===============");
		System.out.println("老系统用户在新系统中没有找到");
		for (Object[] userAttrArray : userAttrArrayList) {
			Integer userId = (Integer) userAttrArray[0];
			String name = (String) userAttrArray[1];
			boolean found = false;
			for (HpsUser user : userList) {
				if (user.getUserName().equals(name)) {
					found = true;
					userMap.put(userId, user);
					break;
				}
			}
			if (!found) {
				System.out.println(+ i++ + ":" + name);
				HpsUser user = new HpsUser();
				User appUser = new User();
				appUser.setEnabled(false);
				user.setUser(appUser);
				user.setBase((HpsBase) session.load(HpsBase.class, 1l));
				user.setType(HpsUserType.ROLE_USER);
				user.setSex(Sex.MALE);
				user.setUserName(name);
				user.setAccountName("OLD_USER_" + (i - 1));
				user.setPassword("123456");
				session.save(user);
				userMap.put(userId, user);
			}
		}
		System.out.println("============check用户结束===============");
	}
	
	private Map<Integer, HpsHouseOwner> houseOwnerMap = new HashMap<Integer, HpsHouseOwner>();
	private Map<Integer,String> oldHouseOwnerNameMap = new HashMap<Integer, String>();
	
	private void checkHouseOwners() {
		String qHouseOwnerStr = "SELECT  [houseMasterID]" // 0
				+ "      ,[houseMasterName]"// 1
				+ "      ,[sex]"// 2
				+ "      ,[IDCard]"// 3
				+ "      ,[intoDatetime]"// 4
				+ "      ,[linkMan]"// 5
				+ "      ,[contact]"// 6
				+ "      ,[statusID]"// 7
				+ "      ,[workUnitID]"// 8
				+ "      ,[wageNum]"// 9
				+ "      ,[regID]"// 10
				+ "  FROM [ZSYJ].[dbo].[houseMaster]";
		SQLQuery qHouseOwnerVB = sessionVB.createSQLQuery(qHouseOwnerStr);
		List<Object[]> ownerAttrArrayList = qHouseOwnerVB.list();
		List<HpsHouseOwner> ownerListAll = session.createCriteria(HpsHouseOwner.class).list();
		List<HpsHouseOwner> ownerList = new ArrayList<HpsHouseOwner>();
		for (HpsHouseOwner owner : ownerListAll) {
			Set<HpsHouse> houses = owner.getHouses();
			boolean found = false;
			for (HpsHouse house : houses) {
				if (house.getLouzuo().getArea().getBase().getId().equals(1l)) {
					found = true;
				}
			}
			if (found) {
				ownerList.add(owner);
			}
		}
		//List<HpsHouseOwner> ownerList = session.createQuery("from HpsHouseOwner where houses.louzuo.area.base.id = 1").list();
		System.out.println("============check房主开始===============");
		int i = 0;
		for (Object[] ownerAttrArray : ownerAttrArrayList) {
			Integer ownerIDVB = (Integer) ownerAttrArray[0];
			String ownerNameVB = (String) ownerAttrArray[1];
			oldHouseOwnerNameMap.put(ownerIDVB, ownerNameVB);
			String wageNumVB = (String) ownerAttrArray[9];
			if (wageNumVB == null || StringUtils.isBlank(wageNumVB)) {
				wageNumVB = "";
			}
			wageNumVB = wageNumVB.trim();
			wageNumVB = wageNumVB.replaceAll("\\(", "（");
			wageNumVB = wageNumVB.replaceAll("（.*维.*）", "");
			Set<HpsHouseOwner> nameSameOwnerList = new HashSet<HpsHouseOwner>();
			Set<HpsHouseOwner> nameWageNumOwnerList = new HashSet<HpsHouseOwner>();
			for (HpsHouseOwner owner : ownerList) {
				String wageNum = owner.getWageNum();
				if (wageNum == null || StringUtils.isBlank(wageNum)) {
					wageNum = "";
				}
				wageNum = wageNum.trim();
				wageNum = wageNum.replaceAll("\\(", "（");
				wageNum = wageNum.replaceAll("（.*维.*）", "");
				if (ownerNameVB.equals(owner.getName()) && wageNumVB.equals(wageNum)) {
					nameWageNumOwnerList.add(owner);
				}
				if (ownerNameVB.equals(owner.getName())) {
					nameSameOwnerList.add(owner);
				}
			}
			if (nameWageNumOwnerList.size() == 1) {
				// 姓名和工资号完全匹配时，正常
				houseOwnerMap.put(ownerIDVB, nameWageNumOwnerList.iterator().next());
			} else if (nameWageNumOwnerList.size() > 1){
				// 姓名和工资号匹配多个，异常
				System.out.println("老系统户主：" + i++ + ownerNameVB + "(工资号： " + wageNumVB +  ")"
						+ "在新系统中发现" + nameWageNumOwnerList.size() + "个完全匹配");
			} else {
				// 姓名和工资号没有匹配
				if (nameSameOwnerList.size() == 1) {
					// 姓名只匹配一个时,正常
					houseOwnerMap.put(ownerIDVB, nameSameOwnerList.iterator().next());
				} else if (nameSameOwnerList.size() > 1) {
					// 姓名和工资号匹配多个，异常
					System.out.println("老系统户主：" +  i++ + ownerNameVB + "(工资号： " + wageNumVB +  ")"
							+ "在新系统中发现" + nameSameOwnerList.size() + "个名称匹配");
				} else {
					// 姓名没有匹配
					System.out.println("老系统户主：" +  i++ + ownerNameVB + "(工资号： " + wageNumVB +  ")"
							+ "在新系统中没有发现匹配");
				}
			}
			
		}
		System.out.println("============check房主结束===============");
	}
	
	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			hpsObject.setCreateTime(currentDate);
			hpsObject.setCreateUsetId(-1l);
			hpsObject.setCreateUserName("OLD_SYS_VB");
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
			hpsObject.setLastUpdateUserName("OLD_SYS_VB");
		}
		return false;
	}
	

}
