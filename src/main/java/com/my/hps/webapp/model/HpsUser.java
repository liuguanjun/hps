package com.my.hps.webapp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.appfuse.model.User;

import com.my.hps.webapp.model.enums.Sex;
import com.my.hps.webapp.model.enums.HpsUserType;

/**
 * 用户
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_user")
public class HpsUser extends HpsBaseObject implements Serializable {
	
	private static final long serialVersionUID = -9115813354885312165L;
	
	private HpsBase base;
	private User user;
	private Sex sex;
	private HpsUserType type;

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
	        .append("base", this.base)
	        .append("userName", this.user.getFirstName())
	        .append("accountName", this.user.getUsername())
	        .append("sex", this.sex)
			.append("type", this.type)
			.append("mobilePhoneNo", this.user.getPhoneNumber());
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return user.equals(o);
	}
	
	@Override
	public int hashCode() {
		return user.hashCode();
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "base_id")
	public HpsBase getBase() {
		return base;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "app_user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	//-------非持久化属性

	@Transient
	public String getAccountName() {
		if (user == null) {
			return "";
		}
		return this.user.getUsername();
	}
	
	public void setAccountName(String accountName) {
		if (user == null) {
			user = new User();
		}
		this.user.setUsername(accountName);
	}
	
	@Transient
	public HpsUserType getType() {
		return type;
	}

	public void setType(HpsUserType type) {
		this.type = type;
	}

	@Transient
	public String getUserName() {
		if (user == null) {
			return "";
		}
		return this.user.getFirstName();
	}
	
	public void setUserName(String userName) {
		if (user == null) {
			user = new User();
		}
		this.user.setFirstName(userName);
	}
	
	@Transient
	public String getMobilePhoneNo() {
		if (user == null) {
			return "";
		}
		return this.user.getPhoneNumber();
	}
	
	public void setMobilePhoneNo(String mobilePhoneNo) {
		if (user == null) {
			user = new User();
		}
		this.user.setPhoneNumber(mobilePhoneNo);
	}
	
	@Transient
	public String getSexStr() {
		if (sex == null) {
			return "";
		}
		return sex.getName();
	}
	
	@Transient
	public String getTypeStr() {
		if (type == null) {
			return "";
		}
		return type.getName();
	}
	
	@Transient
	public String getPassword() {
		if (user == null) {
			return "";
		}
		return this.user.getPassword();
	}
	
	public void setPassword(String password) {
		if (user == null) {
			user = new User();
		}
		this.user.setPassword(password);
	}

}
