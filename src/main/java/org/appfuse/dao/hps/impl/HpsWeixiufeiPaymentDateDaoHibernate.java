package org.appfuse.dao.hps.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsWeixiufeiPaymentDateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsMaintainPaymentDate;
@Repository
public class HpsWeixiufeiPaymentDateDaoHibernate extends HpsGenericDaoHibernate<HpsMaintainPaymentDate, Long> implements HpsWeixiufeiPaymentDateDao {
	
	public HpsWeixiufeiPaymentDateDaoHibernate() {
		super(HpsMaintainPaymentDate.class);
	}
	
	@Override
	public HpsMaintainPaymentDate addPaymentDate(HpsMaintainPaymentDate weixiufeiPaymentDate) {
		Calendar c = new GregorianCalendar();
		int year = c.get(Calendar.YEAR);
		String startStr = year + "0101";
		String endStr = year + "1231";
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			weixiufeiPaymentDate.setPayStartDate(format.parse(startStr));
			weixiufeiPaymentDate.setPayEndDate(format.parse(endStr));
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveOrUpdate(weixiufeiPaymentDate);
        flush();
        return weixiufeiPaymentDate;
	}

	@Override
	public List<HpsMaintainPaymentDate> getPaymentDate(Long baseId) {
        Query qry = createQuery("from HpsMaintainPaymentDate t where t.base.id=:baseId");
        qry.setLong("baseId", baseId);
	    List<HpsMaintainPaymentDate> HpsWeixiufeiPaymentDateList = qry.list();
		return HpsWeixiufeiPaymentDateList;
	}
	
	@Override
	public List<HpsMaintainPaymentDate> getPaymentDateById(Long id) {
        Query qry = createQuery("from HpsMaintainPaymentDate t where t.id=:id");
        qry.setLong("id", id);
	    List<HpsMaintainPaymentDate> HpsWeixiufeiPaymentDateList = qry.list();
		return HpsWeixiufeiPaymentDateList;
	}

	@Override
	public List<HpsMaintainPaymentDate> getPaymentDateByBase(Long baseId,
			String title) {
		String sql = "from HpsMaintainPaymentDate t where t.base.id=:baseId and t.title=:title";
        Query query = createQuery(sql);
        query.setLong("baseId", baseId);
        query.setString("title", title);
        return query.list();
	}

}
