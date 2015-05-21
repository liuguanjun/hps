package com.my.hps.webapp.model.enums;

/**
 * 性别
 * 
 * @author liuguanjun
 *
 */
public enum Sex implements BaseEnum {
	
	MALE("男"),
	FEMALE("女");
	
	private String name;
	
	private Sex(String name) {
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
