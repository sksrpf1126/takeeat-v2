package com.back.takeeat.common;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.market.MarketStatus;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.option.OptionSelect;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
@RequiredArgsConstructor
public class InitMarketDB {

    private final InitMarketService initMarketService;

    @PostConstruct
    public void init() {
        initMarketService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitMarketService {

        private final EntityManager em;

        public void dbInit() {

            Market market = createMarket("청년피자 - 서초직영점",
                    "/images/market-sample.png",
                    "서울 서초구 사임당로 80 (URBANTREE(어반트리))",
                    "지하1층 101호", 37.4893803, 127.0185471,
                    "02-525-9109",
                    "★세상에서 가장 맛있는 도우, ‘청년피자 고메밀크도우’ 출시!★\n" +
                            "도우가 아닌 피자 브랜드 역사상 가장 맛있는 빵을 만들기 위해\n" +
                            "빵의 본질 자체에 집중한, 청년피자의 고집과 집념이 담긴\n" +
                            "고메밀크도우 출시.\n" +
                            "\n" +
                            "우유를 넣어 부드럽고 촉촉한 도우에 버터를 발라 구워 진한 풍미와\n" +
                            "크리스피한 식감을 제공합니다.\n" +
                            "\n" +
                            "완전히 새롭게 달라진, 세상에서 가장 맛있는 도우를 경험해보세요.",
                    "피자·양식", "11:00 ~ 23:00", "177-85-02358",
                    MarketStatus.OPEN, 4.9, 394, "연중무휴"
                    );
            em.persist(market);

            MenuCategory menuCategory01 = createMenuCategory(market, "오리지널 청년피자");
            em.persist(menuCategory01);

            Menu menu01_01 = createMenu(menuCategory01, "바삭한허니비", "바삭한 벌집감자와 달콤한 꿀, 멜팅치즈소스가 만들어낸 단짠단짠 조화", 100, "/images/pizza-sample.jpeg", 18900);
            em.persist(menu01_01);

            OptionCategory optionCategory01_01_01 = createOptionCategory(menu01_01, "피자 사이즈 선택", OptionSelect.SINGLE, 1);
            em.persist(optionCategory01_01_01);
            Option option01_01_01_01 = createOption(optionCategory01_01_01, "R사이즈 (1~2인)", 0);
            em.persist(option01_01_01_01);
            Option option01_01_01_02 = createOption(optionCategory01_01_01, "L사이즈 (2~3인)", 4000);
            em.persist(option01_01_01_02);

            OptionCategory optionCategory01_01_02 = createOptionCategory(menu01_01, "피자토핑 추가", OptionSelect.MULTI, 2);
            em.persist(optionCategory01_01_02);
            Option option01_01_02_01 = createOption(optionCategory01_01_02, "치즈 추가 (80g)", 3000);
            em.persist(option01_01_02_01);
            Option option01_01_02_02 = createOption(optionCategory01_01_02, "페페로니 추가 (20개)", 2000);
            em.persist(option01_01_02_02);
            Option option01_01_02_03 = createOption(optionCategory01_01_02, "옥수수 추가", 1000);
            em.persist(option01_01_02_03);

            Menu menu01_02 = createMenu(menuCategory01, "[인기]할라불고기", "할라피뇨 불고기피자의 원조, 매콤한 할라피뇨와 달짝지근 불고기 위에 뿌려진 매콤달콤한 소스가 일품인 피자", 100, "/images/pizza-sample.jpeg", 19900);
            em.persist(menu01_02);

            OptionCategory optionCategory01_02_01 = createOptionCategory(menu01_02, "크러스트 추가", OptionSelect.SINGLE, 1);
            em.persist(optionCategory01_02_01);
            Option option01_02_01_01 = createOption(optionCategory01_02_01, "기본", 0);
            em.persist(option01_02_01_01);
            Option option01_02_01_02 = createOption(optionCategory01_02_01, "치즈크러스트", 4000);
            em.persist(option01_02_01_02);
            Option option01_02_01_03 = createOption(optionCategory01_02_01, "통치즈크러스트(더욱 꽉 찬)", 5000);
            em.persist(option01_02_01_03);

            OptionCategory optionCategory01_02_02 = createOptionCategory(menu01_02, "사이드메뉴 추가", OptionSelect.MULTI, 3);
            em.persist(optionCategory01_02_02);
            Option option01_02_02_01 = createOption(optionCategory01_02_02, "[신메뉴]갈릭도그스틱", 11900);
            em.persist(option01_02_02_01);
            Option option01_02_02_02 = createOption(optionCategory01_02_02, "[신메뉴]베이컨대파크림치즈스틱", 11900);
            em.persist(option01_02_02_02);
            Option option01_02_02_03 = createOption(optionCategory01_02_02, "알리오올리오파스타 추가", 5900);
            em.persist(option01_02_02_03);

            MenuCategory menuCategory02 = createMenuCategory(market, "프리미엄 청년피자");
            em.persist(menuCategory02);

            Menu menu02_01 = createMenu(menuCategory02, "[신메뉴]메가모짜 치즈디쉬", "치즈가 2배! 위아래 엣지까지 치즈로 덮힌 메가급 치즈디쉬 시리즈, 불향 가득한 필리스테이크의 풍미와 메가 치즈의 조합! 한 입에 느껴지는 풍부한 풍미를 느껴보세요.", 100, "/images/pizza-sample.jpeg", 23900);
            em.persist(menu02_01);

            OptionCategory optionCategory02_01_01 = createOptionCategory(menu02_01, "피자 사이즈 선택", OptionSelect.SINGLE, 1);
            em.persist(optionCategory02_01_01);
            Option option02_01_01_01 = createOption(optionCategory02_01_01, "R사이즈 (1~2인)", 0);
            em.persist(option02_01_01_01);
            Option option02_01_01_02 = createOption(optionCategory02_01_01, "L사이즈 (2~3인)", 4000);
            em.persist(option02_01_01_02);

            OptionCategory optionCategory02_01_02 = createOptionCategory(menu02_01, "피자토핑 추가", OptionSelect.MULTI, 2);
            em.persist(optionCategory02_01_02);
            Option option02_01_02_01 = createOption(optionCategory02_01_02, "치즈 추가 (80g)", 3000);
            em.persist(option02_01_02_01);
            Option option02_01_02_02 = createOption(optionCategory02_01_02, "페페로니 추가 (20개)", 2000);
            em.persist(option02_01_02_02);
            Option option02_01_02_03 = createOption(optionCategory02_01_02, "옥수수 추가", 1000);
            em.persist(option02_01_02_03);
        }

        private Market createMarket(String marketName, String marketImage, String query, String addressDetail,
                                    double latitude, double longitude, String marketNumber, String marketIntroduction, String marketCategory,
                                    String operationTime, String businessNumber, MarketStatus marketStatus, double marketRating,
                                    int reviewCount, String closedDays) {
            return Market.builder()
                    .marketName(marketName)
                    .marketImage(marketImage)
                    .query(query)
                    .addressDetail(addressDetail)
                    .latitude(latitude)
                    .longitude(longitude)
                    .marketNumber(marketNumber)
                    .marketIntroduction(marketIntroduction)
                    .marketCategory(marketCategory)
                    .operationTime(operationTime)
                    .businessNumber(businessNumber)
                    .marketStatus(marketStatus)
                    .marketRating(marketRating)
                    .reviewCount(reviewCount)
                    .closedDays(closedDays)
                    .build();
        }

        private MenuCategory createMenuCategory(Market market, String menuCategoryName) {
            return MenuCategory.builder()
                    .market(market)
                    .menuCategoryName(menuCategoryName)
                    .build();
        }

        private Menu createMenu(MenuCategory menuCategory, String menuName, String menuIntroduction, int menuMaxCount, String menuImage, int menuPrice) {
            return Menu.builder()
                    .menuCategory(menuCategory)
                    .menuName(menuName)
                    .menuIntroduction(menuIntroduction)
                    .menuMaxCount(menuMaxCount)
                    .menuImage(menuImage)
                    .menuPrice(menuPrice)
                    .build();
        }

        private OptionCategory createOptionCategory(Menu menu, String optionCategoryName, OptionSelect optionSelect, int optionMaxCount) {
            return OptionCategory.builder()
                    .menu(menu)
                    .optionCategoryName(optionCategoryName)
                    .optionMaxCount(optionMaxCount)
                    .optionSelect(optionSelect)
                    .build();
        }

        private Option createOption(OptionCategory optionCategory, String optionName, int optionPrice) {
            return Option.builder()
                    .optionCategory(optionCategory)
                    .optionName(optionName)
                    .optionPrice(optionPrice)
                    .build();
        }
    }
}
