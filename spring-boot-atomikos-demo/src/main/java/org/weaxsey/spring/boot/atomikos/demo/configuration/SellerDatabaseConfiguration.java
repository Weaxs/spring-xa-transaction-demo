/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.weaxsey.spring.boot.atomikos.demo.repository.seller.SellerDatasourceProperties;
import org.weaxsey.spring.boot.atomikos.demo.util.XaDataSourceUtil;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = "org.weaxsey.spring.boot.atomikos.demo.repository.seller",
        entityManagerFactoryRef = "sellerEntityManager", transactionManagerRef = "transactionManager")
@EnableConfigurationProperties(SellerDatasourceProperties.class)
public class SellerDatabaseConfiguration {

    @Autowired
    private SellerDatasourceProperties properties;

    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource sellerDataSource()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        XADataSource xaDataSource = XaDataSourceUtil.resolveXaDatasource(properties.getType());
        BeanUtils.copyProperties(properties, xaDataSource);

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setXaDataSource(xaDataSource);
        dataSource.setUniqueResourceName("sellerXaDs");
        return dataSource;
    }

    @Bean
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean sellerEntityManager(DataSource sellerDataSource) throws Throwable {

        Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        jpaPropertyMap.put("javax.persistence.transactionType", "JTA");

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.valueOf(properties.getType().toUpperCase()));

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(sellerDataSource);
        entityManager.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        entityManager.setPackagesToScan("org.weaxsey.spring.boot.atomikos.demo.domain.seller");
        entityManager.setPersistenceUnitName("sellerUnit");
        entityManager.setJpaPropertyMap(jpaPropertyMap);

        return entityManager;
    }

}
