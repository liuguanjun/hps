package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsQunuanfeiTongjiDao;
import org.appfuse.service.hps.HpsQunuanfeiTongjiManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.QunuanfeiTongjiQueryParam;
import com.my.hps.webapp.controller.vo.HpsQunuanfeiTongjiView;
import com.my.hps.webapp.model.HpsHeatingChargeRecord;
import com.my.hps.webapp.model.HpsUser;
@Service
public class HpsQunuanfeiTongjiManagerImpl extends GenericManagerImpl<HpsHeatingChargeRecord, Long> implements HpsQunuanfeiTongjiManager {
	private HpsQunuanfeiTongjiDao qunuanfeiTongjiDao;

	@Autowired
	public void setQunuanfeiTongjiDao(HpsQunuanfeiTongjiDao qunuanfeiTongjiDao) {
		this.qunuanfeiTongjiDao = qunuanfeiTongjiDao;
	}

	@Override
	@Transactional
	public List<HpsUser> getCaozuoyuan(String baseCode) {
		List<HpsUser> caozuoyuanList = qunuanfeiTongjiDao.getCaozuoyuan(baseCode);
		return caozuoyuanList;
	}
	
	@Override
	@Transactional
	public void getQunuanfeiTongjiHushu(
			HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiHushu(qunuanfeiTongjiView, param);
		return;
	}
	
	@Override
	@Transactional
	public void getQunuanfeiTongjiJihuaAndHushu(HpsQunuanfeiTongjiView qunuanfeiTongjiView, 
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiJihuaAndHushu(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiQuanE(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiQuanE(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiHuihou(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiHuihou(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiTinggong(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiTinggong(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiKunnan(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiKunnan(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiZhinajin(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiZhinajin(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiYouhuiJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiYouhuiJianmian(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiTinggongJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiTinggongJianmian(qunuanfeiTongjiView, param);
		return;
	}

	@Override
	@Transactional
	public void getQunuanfeiTongjiKunnanJianmian(HpsQunuanfeiTongjiView qunuanfeiTongjiView,
			QunuanfeiTongjiQueryParam param) {
		qunuanfeiTongjiDao.getQunuanfeiTongjiKunnanJianmian(qunuanfeiTongjiView, param);
		return;
	}

	
	
	
}
