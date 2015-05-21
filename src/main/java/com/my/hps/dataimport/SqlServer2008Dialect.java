package com.my.hps.dataimport;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StringType;

public class SqlServer2008Dialect extends SQLServerDialect {  
	  
    public SqlServer2008Dialect() {  
        super();  
        registerHibernateType(Types.CHAR, StringType.INSTANCE.getName());  
        registerHibernateType(Types.NVARCHAR, StringType.INSTANCE.getName());  
        registerHibernateType(Types.NCHAR, StringType.INSTANCE.getName());  
        registerHibernateType(Types.LONGNVARCHAR, StringType.INSTANCE.getName());  
    }  
}  