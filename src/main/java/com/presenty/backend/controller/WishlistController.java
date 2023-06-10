package com.presenty.backend.controller;

import com.presenty.backend.security.annotation.LoginMemberId;
import com.presenty.backend.service.ChatGptService;
import com.presenty.backend.service.WishlistService;
import com.presenty.backend.service.dto.CreateResult;
import com.presenty.backend.service.dto.WishlistCreateRequest;
import com.presenty.backend.service.dto.WishlistDto;
import com.presenty.backend.service.dto.WishlistRecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final ChatGptService chatGptService;

    private final WishlistService wishlistService;

    @GetMapping("/wishlist/recommend")
    public List<String> getRecommend(@Validated @ModelAttribute WishlistRecommendRequest request) {
        return chatGptService.recommendWishlist(request);
    }

    @PostMapping("/wishlist")
    public CreateResult<Long> createWishlist(
            @RequestBody @Validated WishlistCreateRequest request, @LoginMemberId Long memberId) {
        return wishlistService.create(request, memberId);
    }

    @GetMapping("/wishlist/{memberId}")
    public List<WishlistDto> getWishlists(@PathVariable Long memberId) {
        return wishlistService.getWishlists(memberId);
    }
}
