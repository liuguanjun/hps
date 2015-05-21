package com.my.hps.webapp.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 电费单价
 * 
 * @author liuguanjun
 *
 */
@Entity
@Table(name = "hps_electric_unit")
public class HpsElectricUnit extends HpsBaseObject {
	
	private static final long serialVersionUID = 8337218388708648756L;

	/**
	 * 基地
	 */
	private HpsBase base;
	
	/**
	 * 区域
	 */
	private HpsArea area;
	
	/**
	 * 楼座
	 */
	private HpsLouzuo louzuo;
	
	/**
	 * 单元
	 */
	private String danyuan;
	
	/**
	 * 层
	 */
	private String ceng;
	
	/**
	 * 门牌号
	 */
	private String doorNo;
	
	/**
	 * 用房性质
	 */
	private HpsDictItem yongfangXingzhi;
	
	/**
	 * 是否是阶梯电价
	 */
	private boolean level;
	
	/**
	 * 阶梯1开始
	 */
	private Integer start1;
	
	/**
	 * 阶梯1结束
	 */
	private Integer end1;
	
	/**
	 * 阶梯1电价（元/度）
	 */
	private Double unit1;
	
	/**
	 * 阶梯2开始
	 */
	private Integer start2;
	
	/**
	 * 阶梯2结束
	 */
	private Integer end2;
	
	/**
	 * 阶梯2电价（元/度）
	 */
	private Double unit2;
	
	/**
	 * 阶梯3开始
	 */
	private Integer start3;
	
	/**
	 * 阶梯3结束
	 */
	private Integer end3;
	
	/**
	 * 阶梯3电价（元/度）
	 */
	private Double unit3;
	
	/**
	 * 阶梯4开始
	 */
	private Integer start4;
	
	/**
	 * 阶梯4结束
	 */
	private Integer end4;
	
	/**
	 * 阶梯4电价（元/度）
	 */
	private Double unit4;
	
	/**
	 * 单一电价（元/度）
	 */
	private Double unit;
	
	/**
	 * 卫生费（元/月）
	 */
	private Double weishengfei;
	
	/**
	 * 排污费（元/月）
	 */
	private Double paiwufei;
	
	/**
	 * 照明费（元/月）
	 */
	private Double zhaomingfei;
	
	/**
	 * 是否是历史记录
	 */
	private boolean history;
	
	/**
	 * 电费滞纳金比例（‰/日）
	 */
	private Double zhinaScale;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "base_id")
	public HpsBase getBase() {
		return base;
	}

	public void setBase(HpsBase base) {
		this.base = base;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "area_id")
	public HpsArea getArea() {
		return area;
	}

	public void setArea(HpsArea area) {
		this.area = area;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "louzuo_id")
	public HpsLouzuo getLouzuo() {
		return louzuo;
	}

	public void setLouzuo(HpsLouzuo louzuo) {
		this.louzuo = louzuo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = true, name = "yongfang_xingzhi_id")
	public HpsDictItem getYongfangXingzhi() {
		return yongfangXingzhi;
	}

	public void setYongfangXingzhi(HpsDictItem yongfangXingzhi) {
		this.yongfangXingzhi = yongfangXingzhi;
	}

	@Column(nullable = false)
	public boolean isLevel() {
		return level;
	}

	public void setLevel(boolean level) {
		this.level = level;
	}

	@Column
	public Integer getStart1() {
		return start1;
	}

	public void setStart1(Integer start1) {
		this.start1 = start1;
	}

	@Column
	public Integer getEnd1() {
		return end1;
	}

	public void setEnd1(Integer end1) {
		this.end1 = end1;
	}

	@Column
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getUnit1() {
		return unit1;
	}

	public void setUnit1(Double unit1) {
		this.unit1 = unit1;
	}

	@Column
	public Integer getStart2() {
		return start2;
	}

	public void setStart2(Integer start2) {
		this.start2 = start2;
	}

	@Column
	public Integer getEnd2() {
		return end2;
	}

	public void setEnd2(Integer end2) {
		this.end2 = end2;
	}

	@Column
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getUnit2() {
		return unit2;
	}

	public void setUnit2(Double unit2) {
		this.unit2 = unit2;
	}

	@Column
	public Integer getStart3() {
		return start3;
	}

	public void setStart3(Integer start3) {
		this.start3 = start3;
	}

	@Column
	public Integer getEnd3() {
		return end3;
	}

	public void setEnd3(Integer end3) {
		this.end3 = end3;
	}

	@Column
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getUnit3() {
		return unit3;
	}

	public void setUnit3(Double unit3) {
		this.unit3 = unit3;
	}

	@Column
	public Integer getStart4() {
		return start4;
	}

	public void setStart4(Integer start4) {
		this.start4 = start4;
	}

	@Column
	public Integer getEnd4() {
		return end4;
	}

	public void setEnd4(Integer end4) {
		this.end4 = end4;
	}

	@Column
	public Double getUnit4() {
		return unit4;
	}

	public void setUnit4(Double unit4) {
		this.unit4 = unit4;
	}

	@Column
	@JsonSerialize(using = ElectricMoneySerializer.class)
	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	@Column
	@JsonSerialize(using = MoneySerializer.class)
	public Double getWeishengfei() {
		return weishengfei;
	}

	public void setWeishengfei(Double weishengfei) {
		this.weishengfei = weishengfei;
	}

	@Column
	@JsonSerialize(using = MoneySerializer.class)
	public Double getPaiwufei() {
		return paiwufei;
	}

	public void setPaiwufei(Double paiwufei) {
		this.paiwufei = paiwufei;
	}

	@Column
	@JsonSerialize(using = MoneySerializer.class)
	public Double getZhaomingfei() {
		return zhaomingfei;
	}

	public void setZhaomingfei(Double zhaomingfei) {
		this.zhaomingfei = zhaomingfei;
	}
	
	@Column
	public String getDanyuan() {
		return danyuan;
	}

	public void setDanyuan(String danyuan) {
		this.danyuan = danyuan;
	}

	@Column
	public String getCeng() {
		return ceng;
	}

	public void setCeng(String ceng) {
		this.ceng = ceng;
	}

	@Column(name = "door_no")
	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}
	
	@Column(nullable = false)
	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}
	
	@Column(name = "zhina_scale")
	public Double getZhinaScale() {
		return zhinaScale;
	}

	public void setZhinaScale(Double zhinaScale) {
		this.zhinaScale = zhinaScale;
	}

	@Transient
	public String getDesc() {
		StringBuilder sb = new StringBuilder();
		if (yongfangXingzhi != null) {
			sb.append(yongfangXingzhi.getName());
		}
		sb.append("电价：");
		NumberFormat format = new DecimalFormat("#,##0.000");
		if (level) {
			sb.append(start1).append("~");
			if (end1 != null) {
				sb.append(end1);
			}
			sb.append(" ").append(format.format(unit1)).append(";");
			if (start2 != null) {
				sb.append(start2).append("~");
				if (end2 != null) {
					sb.append(end2);
				}
				sb.append(" ").append(format.format(unit2)).append(";");
			}
			if (start3 != null) {
				sb.append(start3).append("~");
				if (end3 != null) {
					sb.append(end3);
				}
				sb.append(" ").append(format.format(unit3)).append("");
			}
		} else {
			sb.append(format.format(unit)).append("元/度");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", this.id)
			.append("base", this.base == null ? "" : this.base.getCode())
			.append("area", this.area == null ? "" : this.area.getCode())
			.append("louzuo", this.louzuo == null ? "" : this.louzuo.getCode())
	        .append("level", this.level)
	        .append("start1", this.start1)
	        .append("end1", this.end1)
	        .append("unit1", this.unit1)
	        .append("start2", this.start2)
	        .append("end2", this.end2)
	        .append("unit2", this.unit2)
	        .append("start3", this.start3)
	        .append("end3", this.end3)
	        .append("unit3", this.unit3)
	        .append("start4", this.start4)
	        .append("end4", this.end4)
	        .append("unit4", this.unit4)
	        .append("unit", this.unit)
	        .append("danyuan", this.danyuan)
	        .append("ceng", this.ceng)
	        .append("doorNo", this.doorNo)
	        .append("yongfangXingzhi", this.yongfangXingzhi)
	        .append("weishengfei", this.weishengfei)
	        .append("zhaomingfei", this.zhaomingfei)
	        .append("history", this.history);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((ceng == null) ? 0 : ceng.hashCode());
		result = prime * result + ((danyuan == null) ? 0 : danyuan.hashCode());
		result = prime * result + ((doorNo == null) ? 0 : doorNo.hashCode());
		result = prime * result + ((end1 == null) ? 0 : end1.hashCode());
		result = prime * result + ((end2 == null) ? 0 : end2.hashCode());
		result = prime * result + ((end3 == null) ? 0 : end3.hashCode());
		result = prime * result + ((end4 == null) ? 0 : end4.hashCode());
		result = prime * result + (history ? 1231 : 1237);
		result = prime * result + (level ? 1231 : 1237);
		result = prime * result + ((louzuo == null) ? 0 : louzuo.hashCode());
		result = prime * result
				+ ((paiwufei == null) ? 0 : paiwufei.hashCode());
		result = prime * result + ((start1 == null) ? 0 : start1.hashCode());
		result = prime * result + ((start2 == null) ? 0 : start2.hashCode());
		result = prime * result + ((start3 == null) ? 0 : start3.hashCode());
		result = prime * result + ((start4 == null) ? 0 : start4.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((unit1 == null) ? 0 : unit1.hashCode());
		result = prime * result + ((unit2 == null) ? 0 : unit2.hashCode());
		result = prime * result + ((unit3 == null) ? 0 : unit3.hashCode());
		result = prime * result + ((unit4 == null) ? 0 : unit4.hashCode());
		result = prime * result
				+ ((weishengfei == null) ? 0 : weishengfei.hashCode());
		result = prime * result
				+ ((yongfangXingzhi == null) ? 0 : yongfangXingzhi.hashCode());
		result = prime * result
				+ ((zhaomingfei == null) ? 0 : zhaomingfei.hashCode());
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
		HpsElectricUnit other = (HpsElectricUnit) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
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
		if (end1 == null) {
			if (other.end1 != null)
				return false;
		} else if (!end1.equals(other.end1))
			return false;
		if (end2 == null) {
			if (other.end2 != null)
				return false;
		} else if (!end2.equals(other.end2))
			return false;
		if (end3 == null) {
			if (other.end3 != null)
				return false;
		} else if (!end3.equals(other.end3))
			return false;
		if (end4 == null) {
			if (other.end4 != null)
				return false;
		} else if (!end4.equals(other.end4))
			return false;
		if (history != other.history)
			return false;
		if (level != other.level)
			return false;
		if (louzuo == null) {
			if (other.louzuo != null)
				return false;
		} else if (!louzuo.equals(other.louzuo))
			return false;
		if (paiwufei == null) {
			if (other.paiwufei != null)
				return false;
		} else if (!paiwufei.equals(other.paiwufei))
			return false;
		if (start1 == null) {
			if (other.start1 != null)
				return false;
		} else if (!start1.equals(other.start1))
			return false;
		if (start2 == null) {
			if (other.start2 != null)
				return false;
		} else if (!start2.equals(other.start2))
			return false;
		if (start3 == null) {
			if (other.start3 != null)
				return false;
		} else if (!start3.equals(other.start3))
			return false;
		if (start4 == null) {
			if (other.start4 != null)
				return false;
		} else if (!start4.equals(other.start4))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (unit1 == null) {
			if (other.unit1 != null)
				return false;
		} else if (!unit1.equals(other.unit1))
			return false;
		if (unit2 == null) {
			if (other.unit2 != null)
				return false;
		} else if (!unit2.equals(other.unit2))
			return false;
		if (unit3 == null) {
			if (other.unit3 != null)
				return false;
		} else if (!unit3.equals(other.unit3))
			return false;
		if (unit4 == null) {
			if (other.unit4 != null)
				return false;
		} else if (!unit4.equals(other.unit4))
			return false;
		if (weishengfei == null) {
			if (other.weishengfei != null)
				return false;
		} else if (!weishengfei.equals(other.weishengfei))
			return false;
		if (yongfangXingzhi == null) {
			if (other.yongfangXingzhi != null)
				return false;
		} else if (!yongfangXingzhi.equals(other.yongfangXingzhi))
			return false;
		if (zhaomingfei == null) {
			if (other.zhaomingfei != null)
				return false;
		} else if (!zhaomingfei.equals(other.zhaomingfei))
			return false;
		return true;
	}


}
