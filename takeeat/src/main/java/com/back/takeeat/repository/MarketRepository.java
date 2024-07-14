package com.back.takeeat.repository;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.dto.mainPage.response.MarketInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    boolean existsByMarketName(String marketName);

    Optional<Market> findByMemberId(@Param("memberId") Long memberId);

    @Query(
            "SELECT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query " +
                    ", m.addressDetail, m.latitude, m.longitude " +
                    ", m.marketRating, m.reviewCount) " +
            "FROM Market m " +
            "WHERE m.marketCategory = :marketCategory " +
            "AND m.latitude BETWEEN :minLat AND :maxLat " +
            "AND m.longitude BETWEEN :minLon AND :maxLon " +
            "ORDER BY ABS(m.latitude - :latitude), ABS(m.longitude - :longitude) "
    )
    List<MarketInfoResponse> findMarketByLatLon(@Param("marketCategory") String marketCategory,
                                                @Param("minLat") double minLat,
                                                @Param("maxLat") double maxLat,
                                                @Param("minLon") double minLon,
                                                @Param("maxLon") double maxLon,
                                                @Param("latitude") double latitude,
                                                @Param("longitude") double longitude);

    @Query(
            "SELECT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query " +
                    ", m.addressDetail, m.latitude, m.longitude " +
                    ", m.marketRating, m.reviewCount) " +
            "FROM Market m " +
            "WHERE m.latitude BETWEEN :minLat AND :maxLat " +
            "AND m.longitude BETWEEN :minLon AND :maxLon " +
            "ORDER BY ABS(m.latitude - :latitude), ABS(m.longitude - :longitude) "
    )
    List<MarketInfoResponse> findAllMarketByLatLon(@Param("minLat") double minLat,
                                                   @Param("maxLat") double maxLat,
                                                   @Param("minLon") double minLon,
                                                   @Param("maxLon") double maxLon,
                                                   @Param("latitude") double latitude,
                                                   @Param("longitude") double longitude);

    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query, " +
                    "m.addressDetail, m.latitude, m.longitude, m.marketRating, m.reviewCount) " +
            "FROM Market m " +
            "LEFT JOIN m.menuCategories mc " +
            "LEFT JOIN mc.menus mn " +
            "WHERE (m.marketName LIKE %:search% OR mn.menuName LIKE %:search%) " +
            "AND m.latitude BETWEEN :minLat AND :maxLat " +
            "AND m.longitude BETWEEN :minLon AND :maxLon " +
            "ORDER BY ABS(m.latitude - :latitude), ABS(m.longitude - :longitude) "
    )
    List<MarketInfoResponse> findAllMarketByContaining(@Param("minLat") double minLat,
                                                       @Param("maxLat") double maxLat,
                                                       @Param("minLon") double minLon,
                                                       @Param("maxLon") double maxLon,
                                                       @Param("latitude") double latitude,
                                                       @Param("longitude") double longitude,
                                                       @Param("search") String search);

    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query " +
                    ", m.addressDetail, m.latitude, m.longitude " +
                    ", m.marketRating, m.reviewCount) " +
            "FROM Market m " +
            "LEFT JOIN m.menuCategories mc " +
            "LEFT JOIN mc.menus mn " +
            "WHERE m.marketCategory = :marketCategory " +
            "AND (m.marketName LIKE %:search% OR mn.menuName LIKE %:search%) " +
            "AND m.latitude BETWEEN :minLat AND :maxLat " +
            "AND m.longitude BETWEEN :minLon AND :maxLon " +
            "ORDER BY ABS(m.latitude - :latitude), ABS(m.longitude - :longitude) "
    )
    List<MarketInfoResponse> findMarketByContaining(@Param("marketCategory") String marketCategory,
                                                    @Param("minLat") double minLat,
                                                    @Param("maxLat") double maxLat,
                                                    @Param("minLon") double minLon,
                                                    @Param("maxLon") double maxLon,
                                                    @Param("latitude") double latitude,
                                                    @Param("longitude") double longitude,
                                                    @Param("search") String search);


    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query, " +
                    "m.addressDetail, m.latitude, m.longitude, m.marketRating, m.reviewCount) " +
                    "FROM Market m " +
                    "LEFT JOIN m.menuCategories mc " +
                    "LEFT JOIN mc.menus mn " +
                    "WHERE (m.marketName LIKE %:search% OR mn.menuName LIKE %:search%) " +
                    "AND m.latitude BETWEEN :minLat AND :maxLat " +
                    "AND m.longitude BETWEEN :minLon AND :maxLon " +
                    "ORDER BY m.reviewCount DESC"
    )
    List<MarketInfoResponse> findAllByMarketReviewDesc(

            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("search") String search
    );


    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.mainPage.response.MarketInfoResponse(m.id, m.marketName, m.marketImage, m.query, " +
                    "m.addressDetail, m.latitude, m.longitude, m.marketRating, m.reviewCount) " +
                    "FROM Market m " +
                    "LEFT JOIN m.menuCategories mc " +
                    "LEFT JOIN mc.menus mn " +
                    "WHERE (m.marketName LIKE %:search% OR mn.menuName LIKE %:search%) " +
                    "AND m.latitude BETWEEN :minLat AND :maxLat " +
                    "AND m.longitude BETWEEN :minLon AND :maxLon " +
                    "ORDER BY m.marketRating DESC"
    )
    List<MarketInfoResponse> findAllByMarketScoreDesc(@Param("minLat") double minLat,
                                                      @Param("maxLat") double maxLat,
                                                      @Param("minLon") double minLon,
                                                      @Param("maxLon") double maxLon,
                                                      @Param("search") String search);
}