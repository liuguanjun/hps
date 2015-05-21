package com.my.hps.webapp.listener;

import java.sql.Date;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultMergeEventListener;
import org.hibernate.event.spi.MergeEvent;
import org.springframework.stereotype.Component;

import com.my.hps.webapp.model.HpsBaseObject;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.util.SecurityUtil;

@Component
public class HibernateMergeListener extends DefaultMergeEventListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		Object original = event.getOriginal();
		if (original instanceof HpsBaseObject) {
			HpsBaseObject hpsObject = (HpsBaseObject) original;
			HpsUser hpsUser = SecurityUtil.getCurrentUser();
			Date currentDate = new Date(System.currentTimeMillis());
			if (hpsObject.getId() == null) {
				hpsObject.setCreateTime(currentDate);
				hpsObject.setCreateUsetId(hpsUser.getId());
				hpsObject.setCreateUserName(hpsUser.getUserName());
			}
			hpsObject.setLastUpdateTime(currentDate);
			hpsObject.setLastUpdateUserId(hpsUser.getId());
			hpsObject.setLastUpdateUserName(hpsUser.getUserName());
		}
		super.onMerge(event);
	}


	
	

}
