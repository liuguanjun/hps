package com.my.hps.webapp.controller.queryparam;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

public class ElectricUnitQueryParam extends PaginationQueryParam {
	
	private String baseCode;
	private String areaCode;
	private String louzuoCode;
	private String danyuan;
	private String ceng;
	private String yongfangXingzhiCode;
	private String doorNo;
	
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

	public String getYongfangXingzhiCode() {
		return yongfangXingzhiCode;
	}

	public void setYongfangXingzhiCode(String yongfangXingzhiCode) {
		this.yongfangXingzhiCode = yongfangXingzhiCode;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("baseCode", this.baseCode)
	        .append("areaCode", this.areaCode)
	        .append("louzuoCode", this.louzuoCode)
	        .append("danyuan", this.danyuan)
	        .append("ceng", this.ceng)
	        .append("yongfangXingzhiCode", this.yongfangXingzhiCode)
	        .append("doorNo", this.doorNo);
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(baseCode) 
				&& StringUtils.isEmpty(areaCode)
				&& StringUtils.isEmpty(louzuoCode)
				&& StringUtils.isEmpty(danyuan)
				&& StringUtils.isEmpty(ceng)
				&& StringUtils.isEmpty(yongfangXingzhiCode)
				&& StringUtils.isEmpty(doorNo);
	}
	
	

}
