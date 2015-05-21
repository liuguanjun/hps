package com.my.hps.webapp.controller.vo;

public class HpsWeixiufeiTongjiView {
	/**
	 * 收费户数
	 */
	private Long countId;
	/**
	 * 计划总额
	 */
	private String mustChargeSum;
	/**
	 * 实际总额
	 */
	private String actualChargeSum;
	
	public Long getCountId() {
		return countId;
	}
	public void setCountId(Long countId) {
		this.countId = countId;
	}
	public String getMustChargeSum() {
		return mustChargeSum;
	}
	public void setMustChargeSum(String mustChargeSum) {
		this.mustChargeSum = mustChargeSum;
	}
	public String getActualChargeSum() {
		return actualChargeSum;
	}
	public void setActualChargeSum(String actualChargeSum) {
		this.actualChargeSum = actualChargeSum;
	}
}
