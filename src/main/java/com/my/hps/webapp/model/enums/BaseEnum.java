package com.my.hps.webapp.model.enums;

/**
 * 所有枚举类型需要实现的接口
 * 
 * @author liuguanjun
 *
 */
public interface BaseEnum {
	
	/**
	 * 每一个枚举类型都会对应一个编码，此方法返回这个编码
	 * 
	 * @return 枚举类型对应的编码
	 */
	String getCode();
	
	/**
	 * 每一个枚举类型都会对应一个名称，此方法返回这个名称
	 * 
	 * @return 枚举类型对应的名称
	 */
	String getName();

}
