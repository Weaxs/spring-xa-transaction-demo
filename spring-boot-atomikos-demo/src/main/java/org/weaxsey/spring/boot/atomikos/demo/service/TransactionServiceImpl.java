/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weaxsey.spring.boot.atomikos.demo.domain.buyer.Buyer;
import org.weaxsey.spring.boot.atomikos.demo.domain.seller.Seller;
import org.weaxsey.spring.boot.atomikos.demo.repository.buyer.BuyerRepository;
import org.weaxsey.spring.boot.atomikos.demo.repository.seller.SellerRepository;



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
