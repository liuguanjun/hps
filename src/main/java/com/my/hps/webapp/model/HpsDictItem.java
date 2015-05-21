package com.my.hps.webapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hps_dictitem")
public class HpsDictItem extends HpsBaseObject implements Comparable<HpsDictItem>{

	private static final long serialVersionUID = 8082718415721163101L;
	
	private String code;
	private String name;
	private HpsDict dict;
	
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "dict_id")
	@JsonIgnore
	public HpsDict getDict() {
		return dict;
	}

	public void setDict(HpsDict dict) {
		this.dict = dict;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("code", this.code)
	        .append("name", this.name)
			.append("dict", this.dict.getCode());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		HpsDictItem other = (HpsDictItem) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (dict == null) {
			if (other.dict != null)
				return false;
		} else if (!dict.equals(other.dict))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(HpsDictItem o) {
		return (int) (id - o.id);
	}
	
	
}
