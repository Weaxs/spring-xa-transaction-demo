/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.service;

import org.weaxsey.spring.boot.atomikos.demo.domain.buyer.Buyer;
import org.weaxsey.spring.boot.atomikos.demo.domain.seller.Seller;


public interface TransactionService {

    void buyGoods(Buyer buyer, Seller seller, Integer money);
    void buyGoodsWithException(Buyer buyer, Seller seller, Integer money);

}
