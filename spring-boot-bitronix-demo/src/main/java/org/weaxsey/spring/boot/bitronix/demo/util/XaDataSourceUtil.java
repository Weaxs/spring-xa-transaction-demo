/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.bitronix.demo.util;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.orm.jpa.vendor.Database;

import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;


public class XaDataSourceUtil {

    private static final Map<Database, Class<? extends XADataSource>> XA_DATA_SOURCE =
            Map.of(Database.MYSQL, MysqlXADataSource.class,
                    Database.H2, JdbcDataSource.class);

    private static final Map<Database, Class<? extends Driver>> SQL_DRIVER =
            Map.of(Database.MYSQL, com.mysql.jdbc.Driver.class,
                    Database.H2, org.h2.Driver.class);


    public static XADataSource resolveXaDatasource(String dbType)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Database db = Database.valueOf(dbType.toUpperCase());
        if (XA_DATA_SOURCE.containsKey(db)) {
            return XA_DATA_SOURCE.get(db).getDeclaredConstructor().newInstance();
        }
        throw new RuntimeException("no support database type.");
    }

    public static String resolveDriver(String dbType) {
        Database db = Database.valueOf(dbType.toUpperCase());
        if (SQL_DRIVER.containsKey(db)) {
            return SQL_DRIVER.get(db).getSimpleName();
        }
        throw new RuntimeException("no support database type.");
    }

}
