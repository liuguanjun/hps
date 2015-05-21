package com.my.hps.webapp.listener;

import java.sql.Date;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.stereotype.Component;

import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.util.SecurityUtil;

@Component
public class HibernatePreInsertEventListener implements PreInsertEventListener {

	private static final long serialVersionUID = 3263511777347055979L;

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			HpsUser hpsUser = SecurityUtil.getCurrentUser();
			Date currentDate = new Date(System.currentTimeMillis());
			hpsObject.setCreateTime(currentDate);
			if (hpsUser != null) {
				hpsObject.setCreateUsetId(hpsUser.getId());
				hpsObject.setCreateUserName(hpsUser.getUserName());
			} else {
				hpsObject.setCreateUsetId(-1l);
				hpsObject.setCreateUserName("SYS");
			}
		}
		return false;
	}

}
