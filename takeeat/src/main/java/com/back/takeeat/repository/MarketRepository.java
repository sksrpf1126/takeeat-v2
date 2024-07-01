package com.back.takeeat.repository;

import com.back.takeeat.domain.market.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {

}
