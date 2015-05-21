package com.my.hps.webapp.controller.queryparam;

public class PaginationQueryParam {
	
	/**
	 * 页码，从1开始
	 */
	private int page;
	
	/**
	 * 每页显示行数
	 */
	private int rows;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getOffset() {
		return (page - 1) * rows;
	}
	

}
