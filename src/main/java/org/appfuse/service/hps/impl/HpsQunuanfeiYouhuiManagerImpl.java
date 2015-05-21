package org.appfuse.service.hps.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import org.appfuse.dao.hps.HpsQunuanfeiYouhuiDao;
import org.appfuse.service.hps.HpsQunuanfeiYouhuiManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.controller.queryparam.QunuanfeiDiscountQueryParam;
import com.my.hps.webapp.exception.QunuanfeiYouhuiExistsException;
import com.my.hps.webapp.model.HpsHeatingPaymentDatePreferential;
import com.my.hps.webapp.model.HpsHeatingShenfenXingzhiPreferential;

@Service
public class HpsQunuanfeiYouhuiManagerImpl  extends GenericManagerImpl<HpsHeatingShenfenXingzhiPreferential, Long> implements HpsQunuanfeiYouhuiManager {

	private HpsQunuanfeiYouhuiDao qunuanfeiDao;
	
	@Autowired
	public void setQunuanfeiDao(HpsQunuanfeiYouhuiDao qunuanfeiDao) {
		this.qunuanfeiDao = qunuanfeiDao;
	}

	@Override
	@Transactional
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhui(
			QunuanfeiDiscountQueryParam param) {
		List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferential = qunuanfeiDao.getShenfenxingzhiYouhui(param);
		return heatingShenfenXingzhiPreferential;
	}

	@Override
	@Transactional
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhui(
			QunuanfeiDiscountQueryParam param) {
		List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferential = qunuanfeiDao.getPaymentYouhui(param);
		return heatingPaymentDatePreferential;
	}

	@Override
	@Transactional
	public HpsHeatingShenfenXingzhiPreferential addShenfenxingzhiYouhui(
			HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential) {
		// 判断数据库中是否存在
		List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferentialList = 
				qunuanfeiDao.getShenfenxingzhiRecord(
						heatingShenfenXingzhiPreferential.getPaymentDate().getId(),
						heatingShenfenXingzhiPreferential.getShenfengXingzhi().getCode(), 
						heatingShenfenXingzhiPreferential.getPayRate());
		if (heatingShenfenXingzhiPreferentialList.size() != 0) {
			throw new QunuanfeiYouhuiExistsException("身份性质：" + heatingShenfenXingzhiPreferential.getShenfengXingzhi().getName()  + " 已经存在!");
		}
		HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferentialRtn = qunuanfeiDao.addShenfenxingzhiYouhui(heatingShenfenXingzhiPreferential);
		return heatingShenfenXingzhiPreferentialRtn;
	}

	@Override
	@Transactional
	public HpsHeatingPaymentDatePreferential addPaymentYouhui(
			HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential) {
		// 判断数据库中是否存在
		List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferentialList = 
				qunuanfeiDao.getPaymentYouhuiRecord(
						heatingPaymentDatePreferential.getPaymentDate().getId(),
						heatingPaymentDatePreferential.getStartDate(),
						heatingPaymentDatePreferential.getEndDate(),
						heatingPaymentDatePreferential.getOffRate());
		if (heatingPaymentDatePreferentialList.size() != 0) {
			 Format format = new SimpleDateFormat("yyyy-MM-dd");
			throw new QunuanfeiYouhuiExistsException(
					"日期：" + format.format(heatingPaymentDatePreferential.getStartDate()) + 
					"~" + format.format(heatingPaymentDatePreferential.getEndDate()) + " 已经存在!");
		}
		HpsHeatingPaymentDatePreferential heatingPaymentDatePreferentialRtn = qunuanfeiDao.addPaymentYouhui(heatingPaymentDatePreferential);
		return heatingPaymentDatePreferentialRtn;
	}
	
	@Override
	@Transactional
	public HpsHeatingShenfenXingzhiPreferential updateShenfenxingzhiYouhui(
			HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferential) {
		HpsHeatingShenfenXingzhiPreferential heatingShenfenXingzhiPreferentialRtn = qunuanfeiDao.addShenfenxingzhiYouhui(heatingShenfenXingzhiPreferential);
		return heatingShenfenXingzhiPreferentialRtn;
	}

	@Override
	@Transactional
	public HpsHeatingPaymentDatePreferential updatePaymentYouhui(
			HpsHeatingPaymentDatePreferential heatingPaymentDatePreferential) {
		HpsHeatingPaymentDatePreferential heatingPaymentDatePreferentialRtn = qunuanfeiDao.addPaymentYouhui(heatingPaymentDatePreferential);
		return heatingPaymentDatePreferentialRtn;
	}

	@Override
	@Transactional
	public List<HpsHeatingShenfenXingzhiPreferential> getShenfenxingzhiYouhuiById(
			Long shenfenxingzhiYouhuiId) {
		List<HpsHeatingShenfenXingzhiPreferential> heatingShenfenXingzhiPreferential = qunuanfeiDao.getShenfenxingzhiYouhuiById(shenfenxingzhiYouhuiId);
		return heatingShenfenXingzhiPreferential;
	}

	@Override
	@Transactional
	public List<HpsHeatingPaymentDatePreferential> getPaymentYouhuiById(
			Long paymentYouhuiId) {
		List<HpsHeatingPaymentDatePreferential> heatingPaymentDatePreferential = qunuanfeiDao.getPaymentYouhuiById(paymentYouhuiId);
		return heatingPaymentDatePreferential;
	}
}
