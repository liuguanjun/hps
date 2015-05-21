package org.appfuse.service.hps.impl;

import java.util.List;

import org.appfuse.dao.UserDao;
import org.appfuse.dao.hps.HpsUserDao;
import org.appfuse.model.User;
import org.appfuse.service.hps.HpsUserManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.hps.webapp.exception.UserAccountExistsException;
import com.my.hps.webapp.model.HpsUser;

@Service
public class HpsUserManagerImpl extends GenericManagerImpl<HpsUser, Long> implements HpsUserManager {
	
	private PasswordEncoder passwordEncoder;
	private HpsUserDao userDao;
	
	private UserDao appUserDao;
	
	@Override
	@Transactional
	public List<HpsUser> getHpsUsers() {
		return userDao.getHpsUsers();
	}

	@Override
	@Transactional
	public HpsUser getHpsUser(Long userId) {
		return userDao.get(userId);
	}

	@Override
	@Transactional
	public HpsUser saveHpsUser(HpsUser hpsUser) throws UserAccountExistsException {
		User user = hpsUser.getUser();
		boolean passwordChanged = false;
        if (hpsUser.getId() == null) {
            passwordChanged = true;
        } else {
        	String currentPassword = appUserDao.getUserPassword(user.getId());
            if (!currentPassword.equals(user.getPassword())) {
                passwordChanged = true;
            }
        }
        if (passwordChanged) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setEnabled(true);
		try {
			userDao.saveHpsUser(hpsUser);
        } catch (final Exception e) {
            throw new UserAccountExistsException("账户名: '" + hpsUser.getAccountName() 
            		+ "' 已经存在!");
        }
		return hpsUser;
	}


	@Override
	@Transactional
	public void removeHpsUser(Long userId) {
		userDao.remove(userId);
	}

	@Autowired
	public void setUserDao(HpsUserDao userDao) {
		this.dao = userDao;
		this.userDao = userDao;
	}
	
	@Autowired
    @Qualifier("passwordEncoder")
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
    @Autowired
    public void setAppUserDao(UserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

}
