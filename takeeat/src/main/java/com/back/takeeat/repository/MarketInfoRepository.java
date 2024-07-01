package com.back.takeeat.repository;

import com.back.takeeat.domain.market.Market;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarketInfoRepository {

    private final EntityManager em;

    public void saveMarket(Market market) {
        if (market.getId() == null) {
            em.persist(market);
        } else {
            em.merge(market);
        }
    }

}
