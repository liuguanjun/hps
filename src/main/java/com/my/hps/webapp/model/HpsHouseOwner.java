package com.my.hps.webapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 户主
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_house_owner")
public class HpsHouseOwner extends HpsBaseObject {
	
	private static final long serialVersionUID = 6636809154367863007L;

	/**
	 * 房主号
	 */
	private String no;
	
	/**
	 * 房主名
	 */
	private String name;
	
	/**
	 * 身份证号
	 */
	private String idCardNo;
	
	/**
	 * 电话
	 */
	private String phoneNo;
	
	/**
	 * 房主拥有的房屋
	 */
	private Set<HpsHouse> houses;
	
	/**
	 * 工资号
	 */
	private String wageNum;
	
	/**
	 * 备注
	 */
	private String remarks;

	@Column(length = 50)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "idcard_no", length = 50)
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="owner")
	@JsonIgnore
	public Set<HpsHouse> getHouses() {
		return houses;
	}

	public void setHouses(Set<HpsHouse> houses) {
		this.houses = houses;
	}

	@Column(name = "phone_no", length = 50)
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Column(name = "wage_num", length = 100)
	public String getWageNum() {
		return wageNum;
	}

	public void setWageNum(String wageNum) {
		this.wageNum = wageNum;
	}

	@Column(length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", this.id)
		.append(name, this.name)
        .append("wageNum", this.wageNum)
        .append("idCardNo", this.idCardNo);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idCardNo == null) ? 0 : idCardNo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((no == null) ? 0 : no.hashCode());
		result = prime * result + ((wageNum == null) ? 0 : wageNum.hashCode());
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
		HpsHouseOwner other = (HpsHouseOwner) obj;
		if (idCardNo == null) {
			if (other.idCardNo != null)
				return false;
		} else if (!idCardNo.equals(other.idCardNo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (no == null) {
			if (other.no != null)
				return false;
		} else if (!no.equals(other.no))
			return false;
		if (wageNum == null) {
			if (other.wageNum != null)
				return false;
		} else if (!wageNum.equals(other.wageNum))
			return false;
		return true;
	}


}
