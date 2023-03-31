/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.util;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.orm.jpa.vendor.Database;

import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class XaDataSourceUtil {

    private static final Map<Database, Class<? extends XADataSource>> XA_DATA_SOURCE = new HashMap<>();
    static {
        XA_DATA_SOURCE.put(Database.MYSQL, MysqlXADataSource.class);
        XA_DATA_SOURCE.put(Database.H2, JdbcDataSource.class);

    }

    public static XADataSource resolveXaDatasource(String dbType)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Database db = Database.valueOf(dbType.toUpperCase());
        if (XA_DATA_SOURCE.containsKey(db)) {
            return XA_DATA_SOURCE.get(db).getDeclaredConstructor().newInstance();
        }
        throw new RuntimeException("no support database type.");
    }

}
