package com.back.takeeat.repository;

import com.back.takeeat.domain.market.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MarketRepository extends JpaRepository<Market, Long> {

    boolean existsByMarketName(String marketName);

    Market findByMemberId(@Param("memberId") Long memberId);

}
