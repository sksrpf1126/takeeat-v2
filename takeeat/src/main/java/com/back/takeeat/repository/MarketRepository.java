package com.back.takeeat.repository;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    boolean existsByMarketName(String marketName);

    Optional<Market> findByMemberId(@Param("memberId") Long memberId);

}
