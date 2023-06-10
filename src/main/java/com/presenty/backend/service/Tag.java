package com.presenty.backend.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tag {

    LUXURIOUS("고급스러운"),
    CUTE("귀여운"),
    FUNNY("패션 및 뷰티"),
    CLASSY("세련된"),
    COMFORTABLE("편리한"),
    ACTIVE("활동적인"),
    ;

    private final String title;
}
