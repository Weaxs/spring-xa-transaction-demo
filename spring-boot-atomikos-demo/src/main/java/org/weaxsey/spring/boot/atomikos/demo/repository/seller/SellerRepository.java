/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weaxsey.spring.boot.atomikos.demo.domain.seller.Seller;


public interface SellerRepository extends JpaRepository<Seller, Integer> {
}
