/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.bitronix.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weaxsey.spring.boot.bitronix.demo.domain.buyer.Buyer;
import org.weaxsey.spring.boot.bitronix.demo.repository.buyer.BuyerRepository;
import org.weaxsey.spring.boot.bitronix.demo.repository.seller.SellerRepository;
import org.weaxsey.spring.boot.bitronix.demo.domain.seller.Seller;


@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private BuyerRepository buyerRepo;
    @Autowired
    private SellerRepository sellerRepo;

    @Transactional
    @Override
    public void buyGoods(Buyer buyer, Seller seller, Integer money) {
        buyer.setMoney(buyer.getMoney() - money);
        buyerRepo.save(buyer);
        seller.setMoney(seller.getMoney() + money);
        sellerRepo.save(seller);
    }

    @Transactional
    @Override
    public void buyGoodsWithException(Buyer buyer, Seller seller, Integer money) {
        buyer.setMoney(buyer.getMoney() - money);
        buyerRepo.save(buyer);
        seller.setMoney(seller.getMoney() + money);
        sellerRepo.save(seller);
        throw new RuntimeException();
    }


}
