package org.appfuse.dao.hps.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsLouzuoDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsLouzuo;
@Repository
public class HpsLouzuoDaoHibernate extends HpsGenericDaoHibernate<HpsLouzuo, Long> implements HpsLouzuoDao {

	public HpsLouzuoDaoHibernate() {
		super(HpsLouzuo.class);
	}

	@Override
	public HpsLouzuo saveHpsLouzuo(HpsLouzuo louzuo) {
		saveOrUpdate(louzuo);
        flush();
        return louzuo;
	}

	@Override
	public void deleteLouzuo(Long louzuoId) {
		String sql = "delete from HpsLouzuo where id=:louzuoId";
        Query query = this.createQuery(sql);
        query.setLong("louzuoId", louzuoId);
        query.executeUpdate();
		
	}

	@Override
	public List<HpsLouzuo> getHpsLouzuoByArea(Long areaId) {
        Query qry = createQuery("from HpsLouzuo t, HpsArea t1 where t1.id=:areaId and t.area = t1");
        qry.setLong("areaId", areaId);
        List HpsLouzuoList = qry.list();
		List<HpsLouzuo> returnLouzuoList = new ArrayList();
		 Iterator LouzuoIter = HpsLouzuoList.iterator();
		 while (LouzuoIter.hasNext()) {
			 Object[] hpsObject = (Object[]) LouzuoIter.next();
			 HpsLouzuo louzuo = (HpsLouzuo) hpsObject[0];
			 returnLouzuoList.add(louzuo);
		 }

        return returnLouzuoList;
	}
	
	
	@Override
	public List<HpsLouzuo> getHpsLouzuoByBase(Long baseId) {
		Query qry = createQuery("from HpsLouzuo where area.base.id = :baseId");
        qry.setLong("baseId", baseId);
		@SuppressWarnings("unchecked")
		List<HpsLouzuo> returnLouzuoList = qry.list();
        return returnLouzuoList;
	}
	
	@Override
	public List<HpsLouzuo> getLouzuoByCode(String code, Long areaId) {
		String sql = "from HpsLouzuo t, HpsArea t1 where t.code=:code and t.area = t1 and t1.id=:areaId ";
        Query query = this.createQuery(sql);
        query.setString("code", code);
        query.setLong("areaId", areaId);
        
        List HpsLouzuoList = query.list();
		List<HpsLouzuo> returnLouzuoList = new ArrayList();
		 Iterator LouzuoIter = HpsLouzuoList.iterator();
		 while (LouzuoIter.hasNext()) {
			 Object[] hpsObject = (Object[]) LouzuoIter.next();
			 HpsLouzuo louzuo = (HpsLouzuo) hpsObject[0];
			 returnLouzuoList.add(louzuo);
		 }

        return returnLouzuoList;
	}
	
	@Override
	public List<HpsLouzuo> getLouzuoByName(String name, Long areaId) {
		String sql = "from HpsLouzuo t, HpsArea t1 where t.name=:name and t.area = t1 and t1.id=:areaId ";
        Query query = this.createQuery(sql);
        query.setString("name", name);
        query.setLong("areaId", areaId);
        
        List HpsLouzuoList = query.list();
		List<HpsLouzuo> returnLouzuoList = new ArrayList();
		 Iterator LouzuoIter = HpsLouzuoList.iterator();
		 while (LouzuoIter.hasNext()) {
			 Object[] hpsObject = (Object[]) LouzuoIter.next();
			 HpsLouzuo louzuo = (HpsLouzuo) hpsObject[0];
			 returnLouzuoList.add(louzuo);
		 }

        return returnLouzuoList;
	}

	@Override
	public Long getAreaIdByName(String name) {
		String sql = "select id from HpsArea where name=:name";
        Query query = this.createQuery(sql);
        query.setString("name", name);
        List<Long> areaIdList = query.list();
        if (areaIdList.size() > 0) {
        	return (Long)areaIdList.get(0);
        } else {
        	return null;
        }
	}

	@Override
	public List<HpsLouzuo> getLouzuoByAreaCode(String areaCode) {
		Query query = null;
		String sql = "from HpsLouzuo where area.code = :areaCode";
		query = this.createQuery(sql);
		query.setString("areaCode", areaCode);
		@SuppressWarnings("unchecked")
		List<HpsLouzuo> louzuoList = (List<HpsLouzuo>) query.list();
		return louzuoList;
	}

}
