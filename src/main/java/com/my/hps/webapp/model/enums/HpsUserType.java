package com.my.hps.webapp.model.enums;

/**
 * 用户类型
 * 
 * @author liuguanjun
 *
 */
public enum HpsUserType implements BaseEnum {
	
	ROLE_ADMIN("管理员"),
	ROLE_USER("操作员"),
	ROLE_SUPERADMIN("超级管理员"),
	ROLE_HOUSEADMIN("房管员");
	
	private String name;
	
	private HpsUserType(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getCode() {
		return this.toString();
	}

}
