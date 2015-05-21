package com.my.hps.webapp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 区域
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_area")
public class HpsArea extends HpsBaseObject implements Serializable {

	private static final long serialVersionUID = -7541765174671471643L;
	
	private String code;
	private String name;
	private boolean systemInner;
	private HpsBase base;

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("code", this.code)
	        .append("name", this.name)
	        .append("systemInner", this.systemInner)
			.append("base", this.base.getCode());
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (systemInner ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HpsArea other = (HpsArea) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (systemInner != other.systemInner)
			return false;
		return true;
	}



	@Column(nullable = false, name = "code", length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, name = "system_inner")
	public boolean isSystemInner() {
		return systemInner;
	}

	public void setSystemInner(boolean systemInner) {
		this.systemInner = systemInner;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "base_id")
	@JsonIgnore
	public HpsBase getBase() {
		return base;
	}
	
	@Transient
	public String getBaseName() {
		return base.getName();
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}
	
}
