package org.appfuse.dao.hps.impl;

import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsUserDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsUser;

@Repository
public class HpsUserDaoHibernate extends HpsGenericDaoHibernate<HpsUser, Long> implements HpsUserDao {
	
	public HpsUserDaoHibernate() {
        super(HpsUser.class);
    }

	@Override
	public List<HpsUser> getHpsUsers() {
		Query qry = createQuery("from HpsUser u order by upper(u.user.username)");
		List<HpsUser> users = qry.list();
		return users;
	}

	@Override
	public HpsUser saveHpsUser(HpsUser user) {
		saveOrUpdate(user);
        flush();
        return user;
	}

}
