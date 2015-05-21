package com.my.hps.webapp.controller.vo;

public class ResponceError {
	
	private boolean error = true;
	private String errorMsg;
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
