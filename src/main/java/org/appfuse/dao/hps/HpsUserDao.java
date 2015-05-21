package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsUser;

public interface HpsUserDao extends GenericDao<HpsUser, Long> {
	
	List<HpsUser> getHpsUsers();
	
	HpsUser saveHpsUser(HpsUser user);

}
