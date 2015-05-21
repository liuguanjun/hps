package com.my.hps.webapp.controller.queryparam;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户管理页面的查询条件
 * 
 * @author liuguanjun
 *
 */
public class UserQueryParam extends PaginationQueryParam {
	
	private String accountName;
	private String baseCode;
	private String typeCode;
	private String sexCode;
	private String userName;
	private String mobilePhoneNo;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}

	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	
	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("accountName", this.accountName)
	        .append("baseCode", this.baseCode)
	        .append("typeCode", this.typeCode)
	        .append("sexCode", this.typeCode)
	        .append("userName", this.userName)
	        .append("mobildPhoneNo", this.mobilePhoneNo);
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(accountName) 
				&& StringUtils.isEmpty(baseCode)
				&& StringUtils.isEmpty(typeCode)
				&& StringUtils.isEmpty(sexCode)
				&& StringUtils.isEmpty(userName)
				&& StringUtils.isEmpty(mobilePhoneNo);
	}
}
