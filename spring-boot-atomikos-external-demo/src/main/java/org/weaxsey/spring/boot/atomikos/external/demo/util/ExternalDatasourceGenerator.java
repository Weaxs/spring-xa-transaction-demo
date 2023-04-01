/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.external.demo.util;

import com.atomikos.beans.PropertyException;
import com.atomikos.beans.PropertyUtils;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ExternalDatasourceGenerator {

    public static DataSource resolveExternalDataSource(Properties properties)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!properties.containsKey("url")) {
            throw new RuntimeException("there is no url for external database.");
        }

        String[] urls = properties.getProperty("url").split(":", 3);
        String dbType = urls[1];
        XADataSource xaDataSource = XaDataSourceUtil.resolveXaDatasource(dbType);
        try {
            PropertyUtils.setProperties(xaDataSource, properties);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();

        dataSource.setXaDataSource(xaDataSource);
        dataSource.setUniqueResourceName("externalDB" + properties.hashCode() + System.currentTimeMillis());
        return dataSource;

    }


}
