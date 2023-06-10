package com.presenty.backend.service.dto;

import com.presenty.backend.domain.item.Item;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResDto {
    private Long id;
    private String name;
    private int oneoff;
    private Long wishlistId;

    @Builder
    public ItemResDto(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.oneoff = item.getOneoff();
        this.wishlistId = item.getWishlist().getId();
    }
}
