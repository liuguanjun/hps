package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.hps.HpsDictDao;
import org.appfuse.service.hps.HpsDictManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;

@Service
public class HpsDictManagerImpl extends GenericManagerImpl<HpsDict, Long> implements HpsDictManager {
	
	private HpsDictDao dictDao;

	@Override
	@Transactional
	public List<HpsDictItem> getDictItems(String dictCode) {
		List<HpsDictItem> items = dictDao.getDictItems(dictCode);
		return items;
	}

	@Autowired
	public void setDictDao(HpsDictDao dictDao) {
		this.dao = dictDao;
		this.dictDao = dictDao;
	}

	@Override
	@Transactional
	public List<HpsDict> getDicts() {
		return dictDao.getAll();
	}

	@Override
	@Transactional
	public HpsDictItem getShenfenXingzhiDictItem(String itemCode) {
		List<HpsDictItem> items = dictDao.getDictItems(SHENFEN_XINGZHI_CODE);
		for (HpsDictItem item : items) {
			if (item.getCode().equals(itemCode)) {
				return item;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public HpsDictItem getYongfangXingzhiDictItem(String itemCode) {
		List<HpsDictItem> items = dictDao.getDictItems(YONGFANG_XINGZHI_CODE);
		for (HpsDictItem item : items) {
			if (item.getCode().equals(itemCode)) {
				return item;
			}
		}
		return null;
	}
	
	

}
