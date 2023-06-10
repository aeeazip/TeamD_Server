package com.presenty.backend.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Choice {

    SENSITIVE("감성적인"),
    PRACTICAL("실용적인"),
    ;

    private final String title;
}
