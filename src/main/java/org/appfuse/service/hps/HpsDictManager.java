package org.appfuse.service.hps;

import java.util.List;

import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;

public interface HpsDictManager {
	
	String SHENFEN_XINGZHI_CODE = "SHENFEN_XINGZHI";
	String YONGFANG_XINGZHI_CODE = "YONGFANG_XINGZHI";
	
	/**
	 * 电费抄表状态
	 */
	String CHAOBIAO_ZHUANGTAI_CODE ="CHAOBIAO_ZHUANGTAI";
	String CHAOBIAO_ZHUANGTAI_WEICHAO_ITEM_CODE = "WEICHAOBIAO";
	String CHAOBIAO_ZHUANGTAI_YICHAO_ITEM_CODE = "YICHAOBIAO";
	
	String YONGFANG_XINGZHI_SHANGYONG = "COMMERCIAL";
	
	List<HpsDictItem> getDictItems(String dictCode);
	HpsDictItem getShenfenXingzhiDictItem(String itemCode);
	HpsDictItem getYongfangXingzhiDictItem(String itemCode);
	List<HpsDict> getDicts();

}
