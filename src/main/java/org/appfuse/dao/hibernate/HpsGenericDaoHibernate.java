package org.appfuse.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map; 

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.appfuse.dao.SearchException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.my.hps.webapp.dataauth.HpsDataAuthorization;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.HpsUserType;
import com.my.hps.webapp.util.SecurityUtil;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 *      Updated by jgarcia: update hibernate3 to hibernate4
 * @author jgarcia (update: added full text search + reindexing)
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class HpsGenericDaoHibernate<T, PK extends Serializable> {
	
	private static final ThreadLocal<HpsDataAuthorization> authorizationData = new ThreadLocal<HpsDataAuthorization>() {

		@Override
		protected HpsDataAuthorization initialValue() {
			return null;
		}
		
	};
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    protected Class<T> persistentClass;
    @Resource
    private SessionFactory sessionFactory;
    private Analyzer defaultAnalyzer;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public HpsGenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
    }
    
    public static void setCurrentThreadAuthorizationData(HpsDataAuthorization data) {
    	authorizationData.set(data);
    }
    
    public static void removeCurrentThreadAuthorizationData() {
    	authorizationData.remove();
    }

    /**
     * Constructor that takes in a class and sessionFactory for easy creation of DAO.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sessionFactory  the pre-configured Hibernate SessionFactory
     */
    public HpsGenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
    }

//    public SessionFactory getSessionFactory() {
//        return this.sessionFactory;
//    }

    private Session getSession() throws HibernateException {
        Session sess = this.sessionFactory.getCurrentSession();
        if (sess == null) {
            sess = this.sessionFactory.openSession();
        }
        return sess;
    }
    
    //------封装所有session操作
    public Criteria createCriteria(Class<?> persistentClass) {
    	Criteria c = getSession().createCriteria(persistentClass);
    	HpsDataAuthorization data = authorizationData.get();
    	if (data != null) {
    		HpsUser user = SecurityUtil.getCurrentUser();
    		if (user != null && user.getType() != HpsUserType.ROLE_SUPERADMIN) {
    			c.add(Restrictions.eq(data.attrName(), user.getBase().getId()));
    		}
//    		authorizationData.remove();
    	}
    	return c;
    }
    
    public Query createQuery(String queryString) {
    	HpsDataAuthorization data = authorizationData.get();
    	if (data != null) {
    		HpsUser user = SecurityUtil.getCurrentUser();
    		if (user != null && user.getType() != HpsUserType.ROLE_SUPERADMIN) {
    	    	// 简单判断是否有where条件
    	    	if (queryString.toLowerCase().indexOf("where") < 0) {
    	    		queryString += " where " + data.attrName() + " = :baseId";
    	    	} else {
    	    		queryString += " and " + data.attrName() + " = :baseId";
    	    	}
    	    	Query qAuth =  getSession().createQuery(queryString);
    	    	qAuth.setLong("baseId", user.getBase().getId());
//    	    	authorizationData.remove();
    	    	return qAuth;
    		}
    	}
    	Query q =  getSession().createQuery(queryString);
    	return q;
    }
    
    /**
     * 新建一个SQLQuery对象，并且将其返回，目前此方法没有实现数据权限的限定，
     * 需要调用处自行处理
     * 
     * @param queryString
     * @return
     */
    public SQLQuery createSQLQuery(String queryString) {
//    	HpsDataAuthorization data = authorizationData.get();
//    	if (data != null) {
//    		HpsUser user = SecurityUtil.getCurrentUser();
//    		if (user != null && user.getType() != HpsUserType.ROLE_SUPERADMIN) {
//    	    	// 简单判断是否有where条件
//    	    	if (queryString.toLowerCase().indexOf("where") < 0) {
//    	    		queryString += " where " + data.attrName() + " = :baseId";
//    	    	} else {
//    	    		queryString += " and " + data.attrName() + " = :baseId";
//    	    	}
//    	    	Query qAuth =  getSession().createQuery(queryString);
//    	    	qAuth.setLong("baseId", user.getBase().getId());
////    	    	authorizationData.remove();
//    	    	return qAuth;
//    		}
//    	}
    	SQLQuery q =  getSession().createSQLQuery(queryString);
    	return q;
    }
    
    public void saveOrUpdate(Object object) {
    	getSession().saveOrUpdate(object);
    	getSession().flush();
    }
    
    public void flush() {
    	getSession().flush();
    }
    
    public <T1> T1 get(Class<T1> clazz, Serializable id) {
    	IdentifierLoadAccess byId = getSession().byId(clazz);
        T1 entity = (T1) byId.load(id);
    	return entity;
    }
    
    //------

    @Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return createCriteria(persistentClass)
        		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> search(String searchTerm) throws SearchException {
        Session sess = getSession();
        FullTextSession txtSession = Search.getFullTextSession(sess);

        org.apache.lucene.search.Query qry;
        try {
            qry = HibernateSearchTools.generateQuery(searchTerm, this.persistentClass, sess, defaultAnalyzer);
        } catch (ParseException ex) {
            throw new SearchException(ex);
        }
        org.hibernate.search.FullTextQuery hibQuery = txtSession.createFullTextQuery(qry,
                this.persistentClass);
        return hibQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        Session sess = getSession();
        // add by liugj 两个一对一的游离态object，会插入数据库两条记录
        // 很奇怪，这里暂且注释掉merge，用saveOrUpdate代替
        //return (T) sess.merge(object);
        sess.saveOrUpdate(object);
        sess.flush();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        Session sess = getSession();
        sess.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        sess.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Session sess = getSession();
        Query namedQuery = sess.getNamedQuery(queryName);

        for (String s : queryParams.keySet()) {
            namedQuery.setParameter(s, queryParams.get(s));
        }

        return namedQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    public void reindex() {
        HibernateSearchTools.reindex(persistentClass, this.sessionFactory.getCurrentSession());
    }


    /**
     * {@inheritDoc}
     */
    public void reindexAll(boolean async) {
        HibernateSearchTools.reindexAll(async, this.sessionFactory.getCurrentSession());
    }
}
