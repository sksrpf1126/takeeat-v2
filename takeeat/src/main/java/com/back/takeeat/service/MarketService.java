package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.market.request.*;
import com.back.takeeat.dto.market.response.MenuCategoryNameResponse;
import com.back.takeeat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final OptionCategoryRepository optionCategoryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public void marketInfoRegister(MarketInfoRequest marketInfoRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchElementException::new);
        Market market = marketInfoRequest.toMarket(member);
        marketRepository.save(market);
    }

    @Transactional(readOnly = true)
    public boolean checkMarketNameDuplicate(String marketName) {
        return marketRepository.existsByMarketName(marketName);
    }

    @Transactional
    public void menuCategoriesRegister(MenuRequest menuRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchElementException::new);
        Market market = marketRepository.findByMemberId(member.getId());
        for (MarketMenuCategoryRequest marketMenuCategoryRequest : menuRequest.getCategories()) {
            MenuCategory menuCategory = marketMenuCategoryRequest.toMenuCategory(market);

            // 디버깅 포인트: 메뉴 카테고리 정보 출력
            System.out.println("메뉴 카테고리 저장: " + menuCategory.getMenuCategoryName());

            for (MarketMenuRequest marketMenuRequest : marketMenuCategoryRequest.getMenus()) {
                Menu menu = marketMenuRequest.toMenu();

                // 디버깅 포인트: 메뉴 정보 출력
                System.out.println("메뉴 추가: " + menu.getMenuName());

                menu.addMenuCategory(menuCategory);
                menuCategory.getMenus().add(menu);
            }

            menuCategoryRepository.save(menuCategory);

            // 디버깅 포인트: 저장된 메뉴 카테고리 확인
            System.out.println("메뉴 카테고리 저장 완료: " + menuCategory.getId());
        }
    }

    @Transactional
    public List<MenuCategoryNameResponse> getMarketMenuName(Long memberId){
        // 회원 정보를 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));

        // 마켓 정보를 조회
        Market market = marketRepository.findByMemberId(member.getId());
        if (market == null) {
            throw new NoSuchElementException("마켓 정보를 찾을 수 없습니다.");
        }

        // 메뉴 카테고리 정보를 조회
        List<MenuCategoryNameResponse> menuCategoryList = menuCategoryRepository.findMenuCategoriesByMarketId(market.getId());

        // 결과 리스트를 저장할 리스트 초기화
        List<MenuCategoryNameResponse> menuCategoryNameResponses = new ArrayList<>();

        // 각 메뉴 카테고리에 대해 메뉴 정보를 조회하고 결과 리스트에 추가
        for(MenuCategoryNameResponse menuCategoryNameResponse : menuCategoryList) {
            if (!menuCategoryList.contains(menuCategoryNameResponse.getMenuCategoryId())) {
                menuCategoryNameResponses.add(menuCategoryNameResponse.create());
            } else {
                menuCategoryNameResponses.add(menuCategoryList.get(menuCategoryNameResponse.getMenuCategoryId().intValue()));
            }

        }

        return menuCategoryNameResponses;
    }

    @Transactional
    public void optionCategoriesRegister(OptionRequest optionRequest, Long memberId) {
        // 회원 정보를 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));

        // 마켓 정보를 조회
        Market market = marketRepository.findByMemberId(member.getId());
        if (market == null) {
            throw new NoSuchElementException("마켓 정보를 찾을 수 없습니다.");
        }

        List<MenuCategoryNameResponse> menuCategory = menuCategoryRepository.findMenuCategoriesByMarketId(market.getId());

        for (MarketOptionCategoryRequest marketOptionCategoryRequest : optionRequest.getCategories()) {
            OptionCategory optionCategory = marketOptionCategoryRequest.toOptionCategory();

            // 디버깅 포인트: 메뉴 카테고리 정보 출력
            System.out.println("옵션 카테고리 저장: " + optionCategory.getOptionCategoryName());

            for (MarketOptionRequest marketOptionRequest : marketOptionCategoryRequest.getOptions()) {
                Option option = marketOptionRequest.toOption();

                // 디버깅 포인트: 메뉴 정보 출력
                System.out.println("옵션 추가: " + option.getOptionName());

                option.addOptionCategory(optionCategory);
                optionCategory.getOptions().add(option);
            }

            optionCategoryRepository.save(optionCategory);

            // 디버깅 포인트: 저장된 메뉴 카테고리 확인
            System.out.println("메뉴 카테고리 저장 완료: " + optionCategory.getId());
        }
    }
}
