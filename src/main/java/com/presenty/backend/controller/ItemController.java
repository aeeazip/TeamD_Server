package com.presenty.backend.controller;

import com.presenty.backend.service.ItemService;
import com.presenty.backend.service.dto.ItemReqDto;
import com.presenty.backend.service.dto.ItemResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist/{wishlistId}/items")
public class ItemController {
    private final ItemService itemService;

    // 위시리스트 항목 추가
    @PostMapping("")
    public ItemResDto addItem(@PathVariable Long wishlistId, @RequestBody ItemReqDto itemReqDto) {
        ItemResDto getItem = itemService.addItem(wishlistId, itemReqDto);
        return getItem;
    }

    // Item 개수 변경 (선물받은 경우)
    @PostMapping("/{itemId}")
    public ItemResDto updateItemOneoff(@PathVariable Long wishlistId, @PathVariable Long itemId) {
        return itemService.updateItemOneoff(wishlistId, itemId);
    }

    // Taker가 위시리스트에서 Item 삭제
    @DeleteMapping("/{itemId}")
    public String deleteItem(@PathVariable Long wishlistId, @PathVariable Long itemId) {
        return itemService.deleteItem(wishlistId, itemId);
    }
}
