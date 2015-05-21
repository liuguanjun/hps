package com.my.hps.webapp.controller.vo;

import java.util.List;

public class TreeElement {
	
	private String id;
	private String name;
	private List<TreeElement> children;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TreeElement> getChildren() {
		return children;
	}
	public void setChildren(List<TreeElement> children) {
		this.children = children;
	}
	

}
