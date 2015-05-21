package com.my.hps.webapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 电费滞纳金设定
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_electirc_zhinajin_setting")
public class HpsElectricZhinajinSetting extends HpsBaseObject {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 基地
	 */
	private HpsBase base;
	
	/**
	 * 电费滞纳金比例（‰/日）
	 */
	private Double scale;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "base_id")
	public HpsBase getBase() {
		return base;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@Column(name = "scale")
	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((scale == null) ? 0 : scale.hashCode());
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
		HpsElectricZhinajinSetting other = (HpsElectricZhinajinSetting) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (scale == null) {
			if (other.scale != null)
				return false;
		} else if (!scale.equals(other.scale))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("base", this.base.getCode())
			.append("scale", this.scale);
		return sb.toString();
	}
	

}
