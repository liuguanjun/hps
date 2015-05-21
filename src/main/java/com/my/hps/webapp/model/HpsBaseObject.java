package com.my.hps.webapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.appfuse.model.BaseObject;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 所有实体的父类
 * 
 * @author liuguanjun
 *
 */
@MappedSuperclass
public abstract class HpsBaseObject extends BaseObject {
	
	private static final long serialVersionUID = 7723980809516980975L;
	
	protected Long id;
	protected Long createUsetId;
	protected String createUserName;
	protected Date createTime;
	
	protected Long lastUpdateUserId;
	protected String lastUpdateUserName;
	protected Date lastUpdateTime;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "create_user_id")
	public Long getCreateUsetId() {
		return createUsetId;
	}

	public void setCreateUsetId(Long createUsetId) {
		this.createUsetId = createUsetId;
	}

	@Column(length = 50, name = "create_user_name")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Column(name = "create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "last_update_user_id")
	public Long getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(Long lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	@Column(length = 50, name = "last_update_user_name")
	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	@Column(name = "last_update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
