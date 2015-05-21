package com.my.hps.webapp.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 房屋
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_house")
public class HpsHouse extends HpsBaseObject {
	
	private static final long serialVersionUID = -5433339869437495243L;

	/**
	 * 楼座
	 */
	private HpsLouzuo louzuo;
	
	/**
	 * 房号
	 */
	private String no;
	
	/**
	 * 门牌号
	 */
	private String doorNo;
	
	/**
	 * 采暖面积
	 */
	private Double warmArea;
	
	/**
	 * 房屋维修面积
	 */
	private Double repairArea;
	
	/**
	 * 单元
	 */
	private String danyuan;
	
	/**
	 * 层
	 */
	private String ceng;
	
//	/**
//	 * 顺序号
//	 */
//	private String shunxuhao;
	
	/**
	 * 用房性质
	 */
	private HpsDictItem yongfangXingzhi;
	
	/**
	 * 工商注册号
	 */
	private String gongshangNo;
	
	/**
	 * 房主
	 */
	private HpsHouseOwner owner;
	
	/**
	 * 身份性质
	 */
	private HpsDictItem shenfenXingzhi;
	
	/**
	 * 电费抄表
	 */
	private Set<HpsElectricChaobiao> electricityChaobiaoSet;

	@Column(length = 50)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "door_no", length = 50)
	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	@Column(nullable = false, name = "warm_area")
	@JsonSerialize(using = HouseAreaSerializer.class)
	public Double getWarmArea() {
		return warmArea;
	}

	public void setWarmArea(Double warmArea) {
		this.warmArea = warmArea;
	}

	@Column(nullable = false, name = "repair_area")
	@JsonSerialize(using = HouseAreaSerializer.class)
	public Double getRepairArea() {
		return repairArea;
	}

	public void setRepairArea(Double repairArea) {
		this.repairArea = repairArea;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false, name = "louzuo_id")
	public HpsLouzuo getLouzuo() {
		return louzuo;
	}

	public void setLouzuo(HpsLouzuo louzuo) {
		this.louzuo = louzuo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "shenfen_xingzhi_id")
	public HpsDictItem getShenfenXingzhi() {
		return shenfenXingzhi;
	}

	public void setShenfenXingzhi(HpsDictItem shenfenXingzhi) {
		this.shenfenXingzhi = shenfenXingzhi;
	}

	@Column(nullable = false, length = 10)
	public String getDanyuan() {
		return danyuan;
	}

	public void setDanyuan(String danyuan) {
		this.danyuan = danyuan;
	}

	@Column(nullable = false, length = 10)
	public String getCeng() {
		return ceng;
	}

	public void setCeng(String ceng) {
		this.ceng = ceng;
	}

//	@Column(nullable = false, length = 10)
//	public String getShunxuhao() {
//		return shunxuhao;
//	}
//
//	public void setShunxuhao(String shunxuhao) {
//		this.shunxuhao = shunxuhao;
//	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "yongfang_xingzhi_id")
	public HpsDictItem getYongfangXingzhi() {
		return yongfangXingzhi;
	}
	
	public void setYongfangXingzhi(HpsDictItem yongfangXingzhi) {
		this.yongfangXingzhi = yongfangXingzhi;
	}
	
	@Column(name = "gongshang_no", length = 50)
	public String getGongshangNo() {
		return gongshangNo;
	}

	public void setGongshangNo(String gongshangNo) {
		this.gongshangNo = gongshangNo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "owner_id")
	public HpsHouseOwner getOwner() {
		return owner;
	}

	public void setOwner(HpsHouseOwner owner) {
		this.owner = owner;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "house")
	@JsonIgnore
	public Set<HpsElectricChaobiao> getElectricityChaobiaoSet() {
		return electricityChaobiaoSet;
	}

	public void setElectricityChaobiaoSet(Set<HpsElectricChaobiao> electricityChaobiaoSet) {
		this.electricityChaobiaoSet = electricityChaobiaoSet;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", this.id)
		.append("no", this.no)
        .append("warmArea", this.warmArea)
        .append("louzuo", this.louzuo)
		.append("danyuan", this.danyuan)
		.append("ceng", this.ceng);
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ceng == null) ? 0 : ceng.hashCode());
		result = prime * result + ((danyuan == null) ? 0 : danyuan.hashCode());
		result = prime * result + ((doorNo == null) ? 0 : doorNo.hashCode());
		result = prime * result + ((louzuo == null) ? 0 : louzuo.hashCode());
		result = prime * result
				+ ((warmArea == null) ? 0 : warmArea.hashCode());
		result = prime * result
				+ ((yongfangXingzhi == null) ? 0 : yongfangXingzhi.hashCode());
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
		HpsHouse other = (HpsHouse) obj;
		if (ceng == null) {
			if (other.ceng != null)
				return false;
		} else if (!ceng.equals(other.ceng))
			return false;
		if (danyuan == null) {
			if (other.danyuan != null)
				return false;
		} else if (!danyuan.equals(other.danyuan))
			return false;
		if (doorNo == null) {
			if (other.doorNo != null)
				return false;
		} else if (!doorNo.equals(other.doorNo))
			return false;
		if (louzuo == null) {
			if (other.louzuo != null)
				return false;
		} else if (!louzuo.equals(other.louzuo))
			return false;
		if (warmArea == null) {
			if (other.warmArea != null)
				return false;
		} else if (!warmArea.equals(other.warmArea))
			return false;
		if (yongfangXingzhi == null) {
			if (other.yongfangXingzhi != null)
				return false;
		} else if (!yongfangXingzhi.equals(other.yongfangXingzhi))
			return false;
		return true;
	}

	@Transient
	public String getBaseCode() {
		HpsArea area = louzuo.getArea();
		HpsBase base = area.getBase();
		if (base == null) {
			// 默认区域
			return area.getCode().replace("QUYU", "");
		} else {
			return base.getCode();
		}
	}
	
	@Transient
	public String getAddress() {
		HpsArea area = louzuo.getArea();
		StringBuilder sb = new StringBuilder();
		String areaName = area.getName();
		sb.append(area.getBase().getName()).append(" ");
		sb.append(areaName).append("-");
		String louzuoName = louzuo.getName();
		sb.append(louzuoName).append("-");
		sb.append(this.danyuan).append("-");
		sb.append(this.ceng).append("-");
		sb.append(this.doorNo);
		return sb.toString();
	}
	
	@Transient
	public String getShortAddress() {
		return getAddress();
	}
	
	public static class HouseAreaSerializer extends JsonSerializer<Number> {

		@Override
		public void serialize(Number value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			NumberFormat format = new DecimalFormat("#,##0.00");
			jgen.writeString(format.format(value.doubleValue()));
		}
		
	}

}
