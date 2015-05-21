package org.appfuse.dao.hps.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsDictDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;

@Repository
public class HpsDictDaoHibernate extends HpsGenericDaoHibernate<HpsDict, Long> implements HpsDictDao {

	public HpsDictDaoHibernate() {
		super(HpsDict.class);
	}

	@Override
	public List<HpsDictItem> getDictItems(String dictCode) {
		Criteria criteria = createCriteria(persistentClass);
		criteria.add(Restrictions.eq("code", dictCode));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		HpsDict dict = (HpsDict) criteria.uniqueResult();
		List<HpsDictItem> dictItemList = new ArrayList<HpsDictItem>(dict.getItemSet());
		return dictItemList;
	}


}
