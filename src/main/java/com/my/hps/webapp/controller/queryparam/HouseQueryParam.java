package com.my.hps.webapp.controller.queryparam;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

public class HouseQueryParam extends PaginationQueryParam {
	
	private String baseCode;
	private String areaCode;
	private String louzuoCode;
	private String danyuan;
	private String ceng;
	private String ownerName;
	private String shenfenXingzhiCode;
	private String gongshangNo;
	private String yongfangXingzhiCode;
	private String ownerIdCardNo;
	private String houseNo;
	private String ownerNo;
	private String remarks;
	
	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getLouzuoCode() {
		return louzuoCode;
	}
	public void setLouzuoCode(String louzuoCode) {
		this.louzuoCode = louzuoCode;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getShenfenXingzhiCode() {
		return shenfenXingzhiCode;
	}
	public void setShenfenXingzhiCode(String shenfenXingzhiCode) {
		this.shenfenXingzhiCode = shenfenXingzhiCode;
	}
	public String getGongshangNo() {
		return gongshangNo;
	}
	public void setGongshangNo(String gongshangNo) {
		this.gongshangNo = gongshangNo;
	}
	public String getYongfangXingzhiCode() {
		return yongfangXingzhiCode;
	}
	public void setYongfangXingzhiCode(String yongfangXingzhiCode) {
		this.yongfangXingzhiCode = yongfangXingzhiCode;
	}
	public String getOwnerIdCardNo() {
		return ownerIdCardNo;
	}
	public void setOwnerIdCardNo(String ownerIdCardNo) {
		this.ownerIdCardNo = ownerIdCardNo;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("baseCode", this.baseCode)
	        .append("areaCode", this.areaCode)
	        .append("louzuoCode", this.louzuoCode)
	        .append("danyuan", this.danyuan)
	        .append("ceng", this.ceng)
	        .append("ownerName", this.ownerName)
	        .append("shenfenXingzhiCode", this.shenfenXingzhiCode)
	        .append("gongshangNo", this.gongshangNo)
	        .append("yongfangXingzhiCode", this.yongfangXingzhiCode)
	        .append("ownerIdCardNo", this.ownerIdCardNo)
	        .append("houseNo", this.houseNo)
	        .append("remarks", this.remarks)
	        .append("ownerNo", this.ownerNo);
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(baseCode) 
				&& StringUtils.isEmpty(areaCode)
				&& StringUtils.isEmpty(louzuoCode)
				&& StringUtils.isEmpty(danyuan)
				&& StringUtils.isEmpty(ceng)
				&& StringUtils.isEmpty(ownerName)
				&& StringUtils.isEmpty(shenfenXingzhiCode)
				&& StringUtils.isEmpty(gongshangNo)
				&& StringUtils.isEmpty(yongfangXingzhiCode)
				&& StringUtils.isEmpty(ownerIdCardNo)
				&& StringUtils.isEmpty(houseNo)
				&& StringUtils.isEmpty(remarks)
				&& StringUtils.isEmpty(ownerNo);
	}
	
	

}
