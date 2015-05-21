package com.my.hps.webapp.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 基地
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_base")
public class HpsBase extends HpsBaseObject implements Serializable {

	private static final long serialVersionUID = -7541765174671471643L;
	
	private String code;
	private String name;
	private boolean systemInner;
	private Set<HpsArea> areaSet;

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
	        .append("name", this.name)
	        .append("code", this.code)
	        .append("systemInner", this.systemInner);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		HpsBase other = (HpsBase) obj;
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

	@Column(nullable = false, name = "name", length = 50)
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

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="base")
	@OrderBy("id")
	public Set<HpsArea> getAreaSet() {
		return areaSet;
	}

	public void setAreaSet(Set<HpsArea> areaSet) {
		this.areaSet = areaSet;
	}

}
