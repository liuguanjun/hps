package org.appfuse.service.hps;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.my.hps.webapp.exception.UserAccountExistsException;
import com.my.hps.webapp.model.HpsUser;

public interface HpsUserManager extends GenericManager<HpsUser, Long> {
	
	List<HpsUser> getHpsUsers();
	HpsUser getHpsUser(Long userId);
	HpsUser saveHpsUser(HpsUser HpsUser) throws UserAccountExistsException;
	void removeHpsUser(Long userId);

}
