package com.back.takeeat.common;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.market.MarketStatus;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.option.OptionSelect;
import com.back.takeeat.domain.review.OwnerReview;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.review.ReviewImage;
import com.back.takeeat.domain.user.Member;
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

            //가게 및 메뉴 정보
            Market market1 = createMarket("청년피자 - 서초직영점",
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
                    "2", "11:00 ~ 23:00", "177-85-02358",
                    MarketStatus.OPEN, 4.9, 3, "연중무휴"
                    );
            em.persist(market1);

            MenuCategory menuCategory01 = createMenuCategory(market1, "오리지널 청년피자");
            em.persist(menuCategory01);

            Menu menu01_01 = createMenu(menuCategory01, "바삭한허니비", "바삭한 벌집감자와 달콤한 꿀, 멜팅치즈소스가 만들어낸 단짠단짠 조화", 18900);
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

            Menu menu01_02 = createMenu(menuCategory01, "[인기]할라불고기", "할라피뇨 불고기피자의 원조, 매콤한 할라피뇨와 달짝지근 불고기 위에 뿌려진 매콤달콤한 소스가 일품인 피자", 19900);
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

            MenuCategory menuCategory02 = createMenuCategory(market1, "프리미엄 청년피자");
            em.persist(menuCategory02);

            Menu menu02_01 = createMenu(menuCategory02, "[신메뉴]메가모짜 치즈디쉬", "치즈가 2배! 위아래 엣지까지 치즈로 덮힌 메가급 치즈디쉬 시리즈, 불향 가득한 필리스테이크의 풍미와 메가 치즈의 조합! 한 입에 느껴지는 풍부한 풍미를 느껴보세요.", 23900);
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

            Market market2 = createMarket("맥도날드 - 서초뱅뱅점",
                    "서울 서초구 강남대로 305",
                    "서초현대렉시온", 37.4901031, 127.0308569,
                    "02-6227-4500",
                    "일부 매장에서 원재료 수급 문제로 후렌치 후라이 제공이 원활하지 않습니다. 고객님들의 양해를 부탁드리며, 교환가능한 다른 메뉴들을 즐겨 보세요.\n" +
                            "\n" +
                            "매장 별 배달비는 다르게 운영될 수 있으며, 주문 시 결제 페이지에서 미리 확인하실 수 있습니다.\n" +
                            "\n" +
                            "배달 시 가격은 매장과 상이합니다.",
                    "9", "08:00 ~ 00:00", "101-81-26409",
                    MarketStatus.OPEN, 3.9, 4180, "연중무휴"
            );
            em.persist(market2);

            //회원 및 리뷰
            Member member1 = createMember("우디누나", "010-1111-1111");
            em.persist(member1);

            Member member2 = createMember("다이어트는내일부터", "010-2222-2222");
            em.persist(member2);

            Member member3 = createMember("콩나물", "010-3333-3333");
            em.persist(member3);

            Review review1 = createReview(5, "도우도 쫄깃하고 너무 맛있습니다.", member1, market1);
            em.persist(review1);

            Review review2 = createReview(4, "맛있게 잘먹었습니다. 너무 맛있어요.", member2, market1);
            em.persist(review2);

            Review review3 = createReview(5, "굳굳", member1, market1);
            em.persist(review3);

            Review review4 = createReview(5, "역시 맥도날드", member3, market2);
            em.persist(review4);

            OwnerReview ownerReview1 = createOwnerReview("우디누나님, 안녕하세요! 맛있게 드셔주시고 사진도 이쁘게 찍어주셔서 정말 감사합니다. 앞으로도 청년피자 많이 사랑해주세요! 감사합니다!", review1);
            em.persist(ownerReview1);

            OwnerReview ownerReview2 = createOwnerReview("다이어트는내일부터님, 안녕하세요! 정말 감사합니다. 앞으로도 청년피자 많이 사랑해주세요!", review2);
            em.persist(ownerReview2);

            ReviewImage reviewImage1 = createReviewImage(review1);
            em.persist(reviewImage1);

            ReviewImage reviewImage2 = createReviewImage(review1);
            em.persist(reviewImage2);
        }

        private Market createMarket(String marketName, String query, String addressDetail,
                                    double latitude, double longitude, String marketNumber, String marketIntroduction, String marketCategory,
                                    String operationTime, String businessNumber, MarketStatus marketStatus, double marketRating,
                                    int reviewCount, String closedDays) {
            return Market.builder()
                    .marketName(marketName)
                    .marketImage("/images/market-sample.png")
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

        private Menu createMenu(MenuCategory menuCategory, String menuName, String menuIntroduction, int menuPrice) {
            return Menu.builder()
                    .menuCategory(menuCategory)
                    .menuName(menuName)
                    .menuIntroduction(menuIntroduction)
                    .menuImage("/images/pizza-sample.jpeg")
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

        private Member createMember(String nickname, String phone) {
            return Member.builder()
                    .nickname(nickname)
                    .profile("/images/profile.jpg")
                    .phone(phone)
                    .build();
        }

        private Review createReview(int reviewRating, String content, Member member, Market market) {
            return Review.builder()
                    .reviewRating(reviewRating)
                    .content(content)
                    .member(member)
                    .market(market)
                    .build();
        }

        private OwnerReview createOwnerReview(String content, Review review) {
            return OwnerReview.builder()
                    .content(content)
                    .review(review)
                    .build();

        }

        private ReviewImage createReviewImage(Review review) {
            return ReviewImage.builder()
                    .storeName("/images/review-sample.jpg")
                    .review(review)
                    .build();
        }
    }
}
