package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.WeixiufeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsWeixiufeiTongjiView;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsUser;

public interface HpsWeixiufeiTongjiDao extends GenericDao<HpsMaintainChargeRecord, Long> {
	List<HpsUser> getCaozuoyuan(String baseCode);
	// 计划总额,实缴合计
	void getWeixiufeiTongjiJihuaAndHushu(HpsWeixiufeiTongjiView weixiufeiTongjiView, WeixiufeiTongjiQueryParam param);
	// 取得收费户数
	void getWeixiufeiTongjiHushu(HpsWeixiufeiTongjiView weixiufeiTongjiView, WeixiufeiTongjiQueryParam param);
}
