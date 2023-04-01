/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.weaxsey.spring.boot.atomikos.demo.domain.buyer.Buyer;
import org.weaxsey.spring.boot.atomikos.demo.domain.seller.Seller;
import org.weaxsey.spring.boot.atomikos.demo.repository.buyer.BuyerRepository;
import org.weaxsey.spring.boot.atomikos.demo.repository.seller.SellerRepository;
import org.weaxsey.spring.boot.atomikos.demo.service.TransactionService;


@TestPropertySource("classpath:application.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService service;
    @Autowired
    private BuyerRepository buyerRepo;
    @Autowired
    private SellerRepository sellerRepo;

    @Test
    void buyTest() {
        Buyer buyer = new Buyer();
        buyer.setName("buyer-1");
        buyer.setAge(22);
        buyer.setMoney(1000);
        Seller seller = new Seller();
        seller.setName("seller-1");
        seller.setAge(22);
        seller.setMoney(1000);
        service.buyGoods(buyer, seller, 500);

        Assertions.assertNotNull(buyer.getId());
        Assertions.assertNotNull(seller.getId());

    }

    @Test
    void buyWithExceptionTest() {
        Buyer buyer = new Buyer();
        buyer.setName("buyer-2");
        buyer.setAge(22);
        buyer.setMoney(1000);
        Seller seller = new Seller();
        seller.setName("seller-2");
        seller.setAge(22);
        seller.setMoney(1000);
        Assertions.assertThrows(RuntimeException.class, () -> service.buyGoodsWithException(buyer, seller, 500));

        Assertions.assertFalse(buyerRepo.findById(buyer.getId()).isPresent());
        Assertions.assertFalse(sellerRepo.findById(seller.getId()).isPresent());
    }


}
