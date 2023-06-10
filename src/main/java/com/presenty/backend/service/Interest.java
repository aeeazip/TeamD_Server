package com.presenty.backend.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Interest {

    HEALTH_CARE("헬스케어"),
    FOOD_COOKING("음식 및 요리"),
    FASHION_BEAUTY("패션 및 뷰티"),
    GAME("게임"),
    ART_CULTURE("예술 및 문화"),
    HOMELIVING_HOBBY("홈리빙 및 취미"),
    COMPANION_ANIMAL("반려동물"),
    APPLIANCE_DIGITAL("가전 및 디지털"),
    TRAVEL("여행")
    ;

    private final String title;
}
