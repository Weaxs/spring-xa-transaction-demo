/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.external.demo.util;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class XaDataSourceUtil {

    private static final Map<String, Class<? extends XADataSource>> XA_DATA_SOURCE = new HashMap<>();
    static {
        XA_DATA_SOURCE.put("MYSQL", MysqlXADataSource.class);
        XA_DATA_SOURCE.put("H2", JdbcDataSource.class);
    }

    public static XADataSource resolveXaDatasource(String dbType)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String upperCase = dbType.toUpperCase();
        if (XA_DATA_SOURCE.containsKey(upperCase)) {
            return XA_DATA_SOURCE.get(upperCase).getDeclaredConstructor().newInstance();
        }
        throw new RuntimeException("no support database type.");
    }

}
