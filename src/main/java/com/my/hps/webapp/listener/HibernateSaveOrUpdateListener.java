package com.my.hps.webapp.listener;

import java.sql.Date;

import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.stereotype.Component;

import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.util.SecurityUtil;

@Component
public class HibernateSaveOrUpdateListener extends DefaultSaveOrUpdateEventListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		Object entity = event.getObject();
		if (entity instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) entity;
			HpsUser hpsUser = SecurityUtil.getCurrentUser();
			Date currentDate = new Date(System.currentTimeMillis());
			if (hpsObject.getId() == null) {
				hpsObject.setCreateTime(currentDate);
				if (hpsUser != null) {
					hpsObject.setCreateUsetId(hpsUser.getId());
					hpsObject.setCreateUserName(hpsUser.getUserName());
				} else {
					hpsObject.setCreateUsetId(-1l);
					hpsObject.setCreateUserName("SYS");
				}
			} else {
				hpsObject.setLastUpdateTime(currentDate);
				if (hpsUser != null) {
					hpsObject.setLastUpdateUserId(hpsUser.getId());
					hpsObject.setLastUpdateUserName(hpsUser.getUserName());
				} else {
					hpsObject.setLastUpdateUserId(-1l);
					hpsObject.setLastUpdateUserName("SYS");
				}
			}
		}
		super.onSaveOrUpdate(event);
	}
	
	

}
