package com.presenty.backend.controller;

import com.presenty.backend.service.ChatGptService;
import com.presenty.backend.service.dto.WishlistRecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final ChatGptService chatGptService;

    @GetMapping("/wishlist/recommend")
    public List<String> getRecommend(@Validated @ModelAttribute WishlistRecommendRequest request) {
        return chatGptService.recommendWishlist(request);
    }
}
