package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsWeixiufeiTongjiDao;
import org.appfuse.service.hps.HpsWeixiufeiTongjiManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.WeixiufeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsWeixiufeiTongjiView;
import com.my.hps.webapp.model.HpsMaintainChargeRecord;
import com.my.hps.webapp.model.HpsUser;
@Service
public class HpsWeixiufeiTongjiManagerImpl extends GenericManagerImpl<HpsMaintainChargeRecord, Long> implements HpsWeixiufeiTongjiManager {
	private HpsWeixiufeiTongjiDao weixiufeiTongjiDao;

	@Autowired
	public void setWeixiufeiTongjiDao(HpsWeixiufeiTongjiDao weixiufeiTongjiDao) {
		this.weixiufeiTongjiDao = weixiufeiTongjiDao;
	}

	@Override
	@Transactional
	public List<HpsUser> getCaozuoyuan(String baseCode) {
		List<HpsUser> caozuoyuanList = weixiufeiTongjiDao.getCaozuoyuan(baseCode);
		return caozuoyuanList;
	}

	@Override
	@Transactional
	public void getWeixiufeiTongjiJihuaAndHushu(HpsWeixiufeiTongjiView weixiufeiTongjiView, WeixiufeiTongjiQueryParam param) {
		weixiufeiTongjiDao.getWeixiufeiTongjiJihuaAndHushu(weixiufeiTongjiView, param);
		return ;
	}

	@Override
	@Transactional
	public void getWeixiufeiTongjiHushu(
			HpsWeixiufeiTongjiView weixiufeiTongjiView,
			WeixiufeiTongjiQueryParam param) {
		weixiufeiTongjiDao.getWeixiufeiTongjiHushu(weixiufeiTongjiView, param);
		return ;
	}	
}
