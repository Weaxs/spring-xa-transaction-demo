/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weaxsey.spring.boot.atomikos.demo.domain.buyer.Buyer;


public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
}
