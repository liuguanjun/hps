package com.my.hps.webapp.controller.queryparam;


public class ElectricChaobiaoQueryParam extends PaginationQueryParam {
	
	/**
	 * 收取月份
	 */
	private long paymentDateId;
	
	/**
	 * 基地
	 */
	private String base_code;
	
	/**
	 * 区域
	 */
	private String area_code;
	
	/**
	 * 户号
	 */
	private String house_no;
	
	/**
	 * 户名
	 */
	private String houseOwnerName;
	
	/**
	 * 楼座
	 */
	private String louzuo_code;
	
	/**
	 * 单元
	 */
	private String danyuan;
	
	/**
	 * 楼层
	 */
	private String ceng;
	
	/**
	 * 抄表状态:已抄表
	 */
	private boolean recorded;
	
	/**
	 * 缴费状态:已缴费
	 */
	private boolean charged;
	
	/**
	 * 缴费状态:未缴费
	 */
	private boolean unCharged;
	
	/**
	 * 新用电住户
	 */
	private boolean newElectricUser;
	
	/**
	 * 旧用电住户
	 */
	private boolean oldElectricUser;
	
	/**
	 * 抄表状态:未抄表
	 */
	private boolean noRecord;


	public long getPaymentDateId() {
		return paymentDateId;
	}

	public void setPaymentDateId(long paymentDateId) {
		this.paymentDateId = paymentDateId;
	}

	public String getBase_code() {
		return base_code;
	}
	
	public String getHouseOwnerName() {
		return houseOwnerName;
	}

	public void setHouseOwnerName(String houseOwnerName) {
		this.houseOwnerName = houseOwnerName;
	}

	public boolean isRecorded() {
		return recorded;
	}

	public void setRecorded(boolean recorded) {
		this.recorded = recorded;
	}

	public boolean isNoRecord() {
		return noRecord;
	}

	public void setNoRecord(boolean noRecord) {
		this.noRecord = noRecord;
	}

	public void setBase_code(String base_code) {
		this.base_code = base_code;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getHouse_no() {
		return house_no;
	}

	public void setHouse_no(String house_no) {
		this.house_no = house_no;
	}

	public String getLouzuo_code() {
		return louzuo_code;
	}

	public void setLouzuo_code(String louzuo_code) {
		this.louzuo_code = louzuo_code;
	}

	public String getDanyuan() {
		return danyuan;
	}

	public void setDanyuan(String danyuan) {
		this.danyuan = danyuan;
	}

	public String getCeng() {
		return ceng;
	}

	public void setCeng(String ceng) {
		this.ceng = ceng;
	}

	public boolean isCharged() {
		return charged;
	}

	public void setCharged(boolean charged) {
		this.charged = charged;
	}

	public boolean isNewElectricUser() {
		return newElectricUser;
	}

	public void setNewElectricUser(boolean newElectricUser) {
		this.newElectricUser = newElectricUser;
	}

	public boolean isUnCharged() {
		return unCharged;
	}

	public void setUnCharged(boolean unCharged) {
		this.unCharged = unCharged;
	}

	public boolean isOldElectricUser() {
		return oldElectricUser;
	}

	public void setOldElectricUser(boolean oldElectricUser) {
		this.oldElectricUser = oldElectricUser;
	}

}
