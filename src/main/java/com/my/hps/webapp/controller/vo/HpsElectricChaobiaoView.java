package com.my.hps.webapp.controller.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HpsElectricChaobiaoView {
	
	/**
	 * 计费id
	 */
	private Long id;
	
	/**
	 * 计费号
	 */
	private Long chargeNo; 

	/**
	 * 收取月份
	 */
	private String chargeMonth; 
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 上期表值
	 */
	private String provReadoutsElectric;
	
	/**
	 * 本期表值
	 */
	private String readoutsElectric;
	
	/**
	 * 表变更值
	 */
	private String newReadoutsElectric;
	
	/**
	 * 抄表日期
	 */
	private Date readMeterDate;
	
	/**
	 * 户号
	 */
	private String hourseNo;
	
	/**
	 * 户号
	 */
	private Long hourseId;
	
	/**
	 * 户主名
	 */
	private String houseOwnerName;
	
	/**
	 * 基地
	 */
	private String base;
	
	/**
	 * 区域
	 */
	private String area;
	
	/**
	 * 楼座
	 */
	private String louzuo;
	
	/**
	 * 单元
	 */
	private String danyuan;
	
	/**
	 * 楼层
	 */
	private String ceng;
	
	/**
	 * 缴费时间
	 */
	private Date chargeDate;
	
	public Long getChargeNo() {
		return chargeNo;
	}

	public void setChargeNo(Long chargeNo) {
		this.chargeNo = chargeNo;
	}

	public String getChargeMonth() {
		return chargeMonth;
	}

	public void setChargeMonth(String chargeMonth) {
		this.chargeMonth = chargeMonth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvReadoutsElectric() {
		return provReadoutsElectric == null ? "" : provReadoutsElectric;
	}

	public void setProvReadoutsElectric(String provReadoutsElectric) {
		this.provReadoutsElectric = provReadoutsElectric;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getReadMeterDate() {
		return readMeterDate;
	}

	public void setReadMeterDate(Date readMeterDate) {
		this.readMeterDate = readMeterDate;
	}

	public String getHourseNo() {
		return hourseNo;
	}

	public void setHourseNo(String hourseNo) {
		this.hourseNo = hourseNo;
	}

	public String getHouseOwnerName() {
		return houseOwnerName;
	}

	public void setHouseOwnerName(String houseOwnerName) {
		this.houseOwnerName = houseOwnerName;
	}

	public String getReadoutsElectric() {
		return readoutsElectric == null ? "" : readoutsElectric;
	}

	public void setReadoutsElectric(String readoutsElectric) {
		this.readoutsElectric = readoutsElectric;
	}

	public String getNewReadoutsElectric() {
		return newReadoutsElectric == null ? "" : newReadoutsElectric;
	}

	public void setNewReadoutsElectric(String newReadoutsElectric) {
		this.newReadoutsElectric = newReadoutsElectric;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLouzuo() {
		return louzuo;
	}

	public void setLouzuo(String louzuo) {
		this.louzuo = louzuo;
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

	public Long getHourseId() {
		return hourseId;
	}

	public void setHourseId(Long hourseId) {
		this.hourseId = hourseId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	
}
