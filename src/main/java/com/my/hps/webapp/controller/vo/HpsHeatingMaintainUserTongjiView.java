package com.my.hps.webapp.controller.vo;

import java.util.List;

public class HpsHeatingMaintainUserTongjiView {

	private List<HpsHeatingMaintainUserTongjiRowView> rows;
	
	private List<HpsHeatingMaintainUserTongjiRowView> footer;

	public List<HpsHeatingMaintainUserTongjiRowView> getRows() {
		return rows;
	}

	public void setRows(List<HpsHeatingMaintainUserTongjiRowView> rows) {
		this.rows = rows;
	}

	public List<HpsHeatingMaintainUserTongjiRowView> getFooter() {
		return footer;
	}

	public void setFooter(List<HpsHeatingMaintainUserTongjiRowView> footer) {
		this.footer = footer;
	}
	
}
