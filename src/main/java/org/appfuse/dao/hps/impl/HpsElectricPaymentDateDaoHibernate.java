package org.appfuse.dao.hps.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsElectricPaymentDateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsElectricPaymentDate;

@Repository
public class HpsElectricPaymentDateDaoHibernate extends
		HpsGenericDaoHibernate<HpsElectricPaymentDate, Long> implements
		HpsElectricPaymentDateDao {

	public HpsElectricPaymentDateDaoHibernate() {
		super(HpsElectricPaymentDate.class);
	}

	@Override
	public List<HpsElectricPaymentDate> getPaymentDatesByNow(String baseCode) {
		String hql = "from HpsElectricPaymentDate where base.code = :baseCode"
				+ " and month < :month order by month desc";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(format.parse(format.format(new Date())));
		} catch (ParseException e) {
		}
		Query query = createQuery(hql);
		query.setParameter("baseCode", baseCode);
		query.setParameter("month", currentCalendar.getTime());
		return query.list();
	}

	@Override
	public List<HpsElectricPaymentDate> getPaymentDates(String baseCode,
			int year) {
		String hql = "from HpsElectricPaymentDate where base.code = :baseCode"
				+ " and date_format(month,'%Y') = :year order by month desc";
		Query query = createQuery(hql);
		query.setParameter("baseCode", baseCode);
		query.setParameter("year", year + "");
		return query.list();
	}
	
	@Override
	public List<HpsElectricPaymentDate> getPaymentDates(String baseCode) {
		String hql = "from HpsElectricPaymentDate where base.code = :baseCode"
				+ " order by month desc";
		Query query = createQuery(hql);
		query.setParameter("baseCode", baseCode);
		return query.list();
	}

}
