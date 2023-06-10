package com.presenty.backend.service.dto;

import com.presenty.backend.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaperReqDto {
    private Long takerId;
    private String name;
    private String content;
    private Long itemId;
}
