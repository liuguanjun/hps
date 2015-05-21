package com.my.hps.webapp.controller.vo;

public class HpsBaseTreeElement extends TreeElement {
	
	private String systemInner;
	private String bizzDataId;
	private String code;
	private boolean base;
	private String baseCode;

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public String getSystemInner() {
		return systemInner;
	}

	public void setSystemInner(String systemInner) {
		this.systemInner = systemInner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBizzDataId() {
		return bizzDataId;
	}

	public void setBizzDataId(String bizzDataId) {
		this.bizzDataId = bizzDataId;
	}

	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

}
