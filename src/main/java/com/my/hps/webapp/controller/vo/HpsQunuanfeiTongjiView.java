package com.my.hps.webapp.controller.vo;

import java.util.List;

import com.my.hps.webapp.model.HpsHeatingChargeRecord;

public class HpsQunuanfeiTongjiView {
	/**
	 * 收费户数
	 */
	private Long countId;
	/**
	 * 计划总额
	 */
	private String jihuaAll;
	/**
	 * 全额收费(例)
	 */
	private Long quanECount;
	/**
	 * 全额收费额
	 */
	private String quanECharge;
	/**
	 * 惠后收费(例)
	 */
	private Long huihouCount;
	/**
	 * 惠后收费额
	 */
	private String huihouCharge;
	/**
	 * 停供收费(例)
	 */
	private Long tinggongCount;
	/**
	 * 停供收费额
	 */
	private String tinggongCharge;
	/**
	 * 困难住户收费(例)
	 */
	private Long kunnanCount;
	/**
	 * 困难住户收费额
	 */
	private String kunnanCharge;
	/**
	 * 实缴取暖费总额
	 */
	private String shijiaoAllCharge;
	/**
	 * 滞纳缴费(例)
	 */
	private Long zhinajinCount;
	/**
	 * 滞纳缴费额
	 */
	private String zhinajinCharge;
	/**
	 * 优惠减免额
	 */
	private String youhuiJianmianCharge;
	
	/**
	 * 优惠减免额
	 */
	private Double youhuiJianmianChargeDouble;
	
	/**
	 * 停供减免额
	 */
	private String tinggongJianmianCharge;
	
	/**
	 * 停供减免额
	 */
	private Double tinggongJianmianChargeDouble;
	/**
	 * 困难住户减免额
	 */
	private String kunnanJianmianCharge;
	/**
	 * 困难住户减免额
	 */
	private Double kunnanJianmianChargeDouble;
	/**
	 * 减免总额
	 */
	private String jianmianAllCharge;
	public Long getCountId() {
		return countId;
	}
	public void setCountId(Long countId) {
		this.countId = countId;
	}
	public String getJihuaAll() {
		return jihuaAll;
	}
	public void setJihuaAll(String jihuaAll) {
		this.jihuaAll = jihuaAll;
	}
	public Long getQuanECount() {
		return quanECount;
	}
	public void setQuanECount(Long quanECount) {
		this.quanECount = quanECount;
	}
	public String getQuanECharge() {
		return quanECharge;
	}
	public void setQuanECharge(String quanECharge) {
		this.quanECharge = quanECharge;
	}
	public Long getHuihouCount() {
		return huihouCount;
	}
	public void setHuihouCount(Long huihouCount) {
		this.huihouCount = huihouCount;
	}
	public String getHuihouCharge() {
		return huihouCharge;
	}
	public void setHuihouCharge(String huihouCharge) {
		this.huihouCharge = huihouCharge;
	}
	public Long getTinggongCount() {
		return tinggongCount;
	}
	public void setTinggongCount(Long tinggongCount) {
		this.tinggongCount = tinggongCount;
	}
	public String getTinggongCharge() {
		return tinggongCharge;
	}
	public void setTinggongCharge(String tinggongCharge) {
		this.tinggongCharge = tinggongCharge;
	}
	public Long getKunnanCount() {
		return kunnanCount;
	}
	public void setKunnanCount(Long kunnanCount) {
		this.kunnanCount = kunnanCount;
	}
	public String getKunnanCharge() {
		return kunnanCharge;
	}
	public void setKunnanCharge(String kunnanCharge) {
		this.kunnanCharge = kunnanCharge;
	}
	public String getShijiaoAllCharge() {
		return shijiaoAllCharge;
	}
	public void setShijiaoAllCharge(String shijiaoAllCharge) {
		this.shijiaoAllCharge = shijiaoAllCharge;
	}
	public Long getZhinajinCount() {
		return zhinajinCount;
	}
	public void setZhinajinCount(Long zhinajinCount) {
		this.zhinajinCount = zhinajinCount;
	}
	public String getZhinajinCharge() {
		return zhinajinCharge;
	}
	public void setZhinajinCharge(String zhinajinCharge) {
		this.zhinajinCharge = zhinajinCharge;
	}
	public String getYouhuiJianmianCharge() {
		return youhuiJianmianCharge;
	}
	public void setYouhuiJianmianCharge(String youhuiJianmianCharge) {
		this.youhuiJianmianCharge = youhuiJianmianCharge;
	}
	public String getTinggongJianmianCharge() {
		return tinggongJianmianCharge;
	}
	public void setTinggongJianmianCharge(String tinggongJianmianCharge) {
		this.tinggongJianmianCharge = tinggongJianmianCharge;
	}
	public String getKunnanJianmianCharge() {
		return kunnanJianmianCharge;
	}
	public void setKunnanJianmianCharge(String kunnanJianmianCharge) {
		this.kunnanJianmianCharge = kunnanJianmianCharge;
	}
	public String getJianmianAllCharge() {
		return jianmianAllCharge;
	}
	public void setJianmianAllCharge(String jianmianAllCharge) {
		this.jianmianAllCharge = jianmianAllCharge;
	}
	public Double getYouhuiJianmianChargeDouble() {
		return youhuiJianmianChargeDouble;
	}
	public void setYouhuiJianmianChargeDouble(Double youhuiJianmianChargeDouble) {
		this.youhuiJianmianChargeDouble = youhuiJianmianChargeDouble;
	}
	public Double getTinggongJianmianChargeDouble() {
		return tinggongJianmianChargeDouble;
	}
	public void setTinggongJianmianChargeDouble(Double tinggongJianmianChargeDouble) {
		this.tinggongJianmianChargeDouble = tinggongJianmianChargeDouble;
	}
	public Double getKunnanJianmianChargeDouble() {
		return kunnanJianmianChargeDouble;
	}
	public void setKunnanJianmianChargeDouble(Double kunnanJianmianChargeDouble) {
		this.kunnanJianmianChargeDouble = kunnanJianmianChargeDouble;
	}
}
