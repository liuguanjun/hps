package com.my.hps.webapp.listener;

import java.sql.Date;

import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.stereotype.Component;

import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.util.SecurityUtil;

@Component
public class HibernatePreUpdateEventListener implements PreUpdateEventListener {

	private static final long serialVersionUID = 3982810576807569225L;

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			HpsUser hpsUser = SecurityUtil.getCurrentUser();
			Date currentDate = new Date(System.currentTimeMillis());
			hpsObject.setLastUpdateTime(currentDate);
			if (hpsUser != null) {
				hpsObject.setLastUpdateUserId(hpsUser.getId());
				hpsObject.setLastUpdateUserName(hpsUser.getUserName());
			} else {
				hpsObject.setLastUpdateUserId(-1l);
				hpsObject.setLastUpdateUserName("SYS");
			}
		}
		return false;
	}

}
