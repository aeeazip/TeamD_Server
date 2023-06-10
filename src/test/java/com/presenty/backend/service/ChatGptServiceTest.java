package com.presenty.backend.service;

import com.presenty.backend.service.dto.WishlistRecommendRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

class ChatGptServiceTest {

    @Test
    void practice() {

        ChatGptService service = new ChatGptService("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");

        WishlistRecommendRequest request = new WishlistRecommendRequest();
        request.setInterests(List.of(Interest.GAME, Interest.TRAVEL));
        request.setTags(List.of(Tag.COMFORTABLE, Tag.CUTE));
        request.setChoice(Choice.PRACTICAL);
        System.out.println(service.recommendWishlist(request));
    }
}