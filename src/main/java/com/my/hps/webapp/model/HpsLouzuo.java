package com.my.hps.webapp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 区域
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_louzuo")
public class HpsLouzuo extends HpsBaseObject implements Serializable, Comparable<HpsLouzuo> {

	private static final long serialVersionUID = -7541765174671471643L;
	
	private String code;
	private String name;
	private boolean systemInner;
	private HpsArea area;
	

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("code", this.code)
	        .append("name", this.name)
	        .append("systemInner", this.systemInner)
			.append("area", this.area.getCode());
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
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
		HpsLouzuo other = (HpsLouzuo) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
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
	@JoinColumn(nullable = false, name = "area_id")
	public HpsArea getArea() {
		return area;
	}
	
	public void setArea(HpsArea area) {
		this.area = area;
	}

	@Override
	public int compareTo(HpsLouzuo o) {
		if (this.id == o.id) {
			return 0;
		}
		String myName = this.name;
		if (myName == null) {
			myName = "";
		}
		String otherName = o.name;
		if (otherName == null) {
			otherName = "";
		}
		if (myName.length() < 5) {
			myName = StringUtils.leftPad(myName, 5, "0");
		}
		if (otherName.length() < 5) {
			otherName = StringUtils.leftPad(otherName, 5, "0");
		}
		Long myBaseId = this.area.getBase().getId();
		if (myBaseId == null) {
			myBaseId = 0l;
		}
		Long otherBaseId = o.area.getBase().getId();
		if (otherBaseId == null) {
			otherBaseId = 0l;
		}
		Long myAreaId = this.area.getId();
		if (myAreaId == null) {
			myAreaId = 0l;
		}
		Long otherAreaId = o.area.getId();
		if (otherAreaId == null) {
			otherAreaId = 0l;
		}
		myName = 100000 + (myBaseId * 1000) + (myAreaId * 10) + myName;
		otherName = 100000 + (otherBaseId * 1000) + (otherAreaId * 10) + otherName;
		return myName.compareTo(otherName);
	}

}
