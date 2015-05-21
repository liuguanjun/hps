package org.appfuse.dao.hps.impl;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.appfuse.dao.hps.HpsHeatingPaymentDateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.my.hps.webapp.model.HpsHeatingPaymentDate;
@Repository
public class HpsHeatingPaymentDateDaoHibernate extends HpsGenericDaoHibernate<HpsHeatingPaymentDate, Long> implements HpsHeatingPaymentDateDao {
	
	public HpsHeatingPaymentDateDaoHibernate() {
		super(HpsHeatingPaymentDate.class);
	}
	
	@Override
	public HpsHeatingPaymentDate addPaymentDateAndZhinajin(HpsHeatingPaymentDate heatingPaymentDate) {
		saveOrUpdate(heatingPaymentDate);
        flush();
        return heatingPaymentDate;
	}

	@Override
	public List<HpsHeatingPaymentDate> getPaymentDateByBaseAndDate(Long baseId, String title, Date startDate,Date endDate) {
		String sql = "from HpsHeatingPaymentDate t where t.base.id=:baseId and t.title=:title and t.payStartDate=:startDate and t.payEndDate=:endDate ";
        Query query = createQuery(sql);
        query.setLong("baseId", baseId);
        query.setString("title", title);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return query.list();
	}

	@Override
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajin(Long baseId) {
        Query qry = createQuery("from HpsHeatingPaymentDate t where t.base.id=:baseId");
        qry.setLong("baseId", baseId);
	    List<HpsHeatingPaymentDate> HpsHeatingPaymentDateList = qry.list();
		return HpsHeatingPaymentDateList;
	}
	
	@Override
	public List<HpsHeatingPaymentDate> getPaymentDateAndZhinajinById(Long id) {
        Query qry = createQuery("from HpsHeatingPaymentDate t where t.id=:id");
        qry.setLong("id", id);
	    List<HpsHeatingPaymentDate> HpsHeatingPaymentDateList = qry.list();
		return HpsHeatingPaymentDateList;
	}
}
