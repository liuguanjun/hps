package com.my.hps.webapp.controller.vo;

import java.util.List;

public class HpsElectricUserTongjiView {

	private List<HpsElectricUserTongjiRowView> rows;
	
	private List<HpsElectricUserTongjiRowView> footer;

	public List<HpsElectricUserTongjiRowView> getRows() {
		return rows;
	}

	public void setRows(List<HpsElectricUserTongjiRowView> rows) {
		this.rows = rows;
	}

	public List<HpsElectricUserTongjiRowView> getFooter() {
		return footer;
	}

	public void setFooter(List<HpsElectricUserTongjiRowView> footer) {
		this.footer = footer;
	}
	
}
