/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.bitronix.demo.configuration;

import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;


@Configuration
public class TransactionConfiguration {

    @Bean(name = "bitronixTransactionManager")
    public TransactionManager bitronixTransactionManager() {
        return TransactionManagerServices.getTransactionManager();
    }

    @Bean(name = "transactionManager")
    @DependsOn({ "bitronixTransactionManager" })
    public PlatformTransactionManager transactionManager(TransactionManager bitronixTransactionManager) {

        return new JtaTransactionManager(bitronixTransactionManager);
    }


}
