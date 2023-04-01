package org.weaxsey.spring.boot.atomikos.external.demo;/*
 * Copyright (c) 2023, Weaxs
 *
 */

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.weaxsey.spring.boot.atomikos.external.demo.Application;
import org.weaxsey.spring.boot.atomikos.external.demo.service.ExternalDatabaseService;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.SQLException;
import java.util.Properties;


@SpringBootTest(classes = Application.class)
public class ExternalServiceTest {

    static final Properties BUYER_PROPS = new Properties();
    static final Properties SELLER_PROPS = new Properties();
    static final String DB_URL_FIELD = "url";
    static final String DB_USER_FIELD = "user";
    static final String DB_PASSWORD_FIELD = "password";

    @Autowired
    private UserTransaction userTransaction;
    @Autowired
    private ExternalDatabaseService service;


    @BeforeAll
    static void init() {
        BUYER_PROPS.setProperty(DB_URL_FIELD, "jdbc:mysql://localhost:3306/buyer");
        BUYER_PROPS.setProperty(DB_USER_FIELD, "root");
        BUYER_PROPS.setProperty(DB_PASSWORD_FIELD, "12345678");

        SELLER_PROPS.setProperty(DB_URL_FIELD, "jdbc:mysql://localhost:3306/seller");
        SELLER_PROPS.setProperty(DB_USER_FIELD, "root");
        SELLER_PROPS.setProperty(DB_PASSWORD_FIELD, "12345678");
    }

    @Test
    void buyGoodsTest()
            throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException, SQLException {
        userTransaction.begin();
        service.executeExternalSql(BUYER_PROPS,
                "INSERT INTO buyer(name, age, money) VALUE ('external-buyer-1', 24, 1000)");
        service.executeExternalSql(SELLER_PROPS,
                "INSERT INTO seller(name, age, money) VALUE ('external-seller-1', 24, 1000)");
        userTransaction.commit();

        JsonNode buyerRs = service.executeExternalSql(BUYER_PROPS,
                "SELECT id,name,age,money FROM buyer WHERE name = 'external-buyer-1'");
        JsonNode sellerRs = service.executeExternalSql(SELLER_PROPS,
                "SELECT id,name,age,money FROM seller WHERE name = 'external-seller-1'");
        Assertions.assertEquals(1, buyerRs.size());
        Assertions.assertEquals(1, sellerRs.size());
    }

    @Test
    void buyGoodsWithThrowableTest()
            throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException, SQLException {
       try {
           userTransaction.begin();
           service.executeExternalSql(BUYER_PROPS,
                   "INSERT INTO buyer(name, age, money) VALUE ('external-buyer-2', 24, 1000)");
           service.executeExternalSql(SELLER_PROPS,
                   "INSERT INTO seller(name, age, money) VALUE ('external-seller-2', 24, 1000)");
           throw new RuntimeException();
       } catch (RuntimeException e) {
           userTransaction.rollback();
       }

        JsonNode buyerRsCnt = service.executeExternalSql(BUYER_PROPS,
                "SELECT id,name,age,money FROM buyer WHERE name = 'external-buyer-2'");
        JsonNode sellerRsCnt = service.executeExternalSql(SELLER_PROPS,
                "SELECT id,name,age,money FROM seller WHERE name = 'external-seller-2'");
        Assertions.assertEquals(0, buyerRsCnt.size());
        Assertions.assertEquals(0, sellerRsCnt.size());
    }








}
