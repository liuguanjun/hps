package org.appfuse.dao.hps.impl;

import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsBaseDao;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.dataauth.HpsDataAuthorization;
import com.my.hps.webapp.dataauth.HpsDataAuthorization.TargetType;
import com.my.hps.webapp.model.HpsArea;
import com.my.hps.webapp.model.HpsBase;

@Repository
public class HpsBaseDaoHibernate extends HpsGenericDaoHibernate<HpsBase, Long> 
		implements HpsBaseDao {
	
	public HpsBaseDaoHibernate() {
        super(HpsBase.class);
    }

	@Override
	public HpsBase getBaseByCode(String code) {
		HpsBase base = (HpsBase) createCriteria(HpsBase.class)
				.add(Restrictions.eq("code", code)).uniqueResult();
		return base;
	}
	
	@Override
	public HpsBase getBaseById(Long id) {
		HpsBase base = (HpsBase) createCriteria(HpsBase.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return base;
	}
	
	@Override
	public void deleteBaseTreeElement(Long areaId) {
		String sql = "delete from HpsArea where id=:areaId";
        Query query = this.createQuery(sql);
        query.setLong("areaId", areaId);
        query.executeUpdate();

	}
	
	@Override
	public List<HpsArea> getAreaByCode(String code, Long baseId) {
		String sql = "from HpsArea where code=:code and base_id=:baseId ";
        Query query = this.createQuery(sql);
        query.setString("code", code);
        query.setLong("baseId", baseId);
        
        List<HpsArea> hpsAreaList = query.list();
        return hpsAreaList;
	}
	
	@Override
	public List<HpsArea> getAreaByName(String name, Long baseId) {
		String sql = "from HpsArea where name=:name and base_id=:baseId ";
        Query query = this.createQuery(sql);
        query.setString("name", name);
        query.setLong("baseId", baseId);
        
        List<HpsArea> hpsAreaList = query.list();
        return hpsAreaList;
	}
	
	@Override
	public HpsArea saveHpsArea(HpsArea area) {
		saveOrUpdate(area);
        flush();
        return area;
	}
	
	@HpsDataAuthorization(type = TargetType.Criteria, attrName = "id")
    public List<HpsBase> getAll() {
        return super.getAll();
    }

}
