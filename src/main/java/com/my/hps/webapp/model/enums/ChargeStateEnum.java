package com.my.hps.webapp.model.enums;

/**
 * 缴费状态
 * 
 * @author liuguanjun
 *
 */
public enum ChargeStateEnum implements BaseEnum {
	
	CHARGED("已缴费"),
	UNCHARGED("未缴费"),
	CANCELLED("已取消");
	
	private String name;
	
	private ChargeStateEnum(String name) {
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
