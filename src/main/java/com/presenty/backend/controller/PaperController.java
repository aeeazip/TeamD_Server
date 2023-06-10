package com.presenty.backend.controller;

import com.presenty.backend.service.ChatGptService;
import com.presenty.backend.service.ItemService;
import com.presenty.backend.service.dto.PaperReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.presenty.backend.service.dto.PaperResDto;
import com.presenty.backend.service.PaperService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController {
    private final PaperService paperService;

    private final ChatGptService chatGptService;
    private final ItemService itemService;

    // mbti 메시지 추천 결과 제공
    @GetMapping("/recommend/{takerId}")
    public List<String> recommendPaper(@PathVariable Long takerId) {
        return chatGptService.recommendPaper(takerId);
    }


    // 롤링 페이퍼 등록
    @PostMapping("/{wishlistId}/register/{giverId}")
    public PaperResDto createPaper(@PathVariable Long wishlistId, @PathVariable Long giverId, @RequestBody PaperReqDto paperReqDto) {
        PaperResDto newPaper = paperService.createPaper(wishlistId, giverId, paperReqDto);
        itemService.deleteItem(newPaper.getWishlistId(), newPaper.getItemId());
        return newPaper;
    }

    // 특정 위시리스트 안에 있는 롤링페이퍼 전체 조회
    @GetMapping("/{wishlistId}")
    public List<PaperResDto> getPapers(@PathVariable Long wishlistId) {
        List<PaperResDto> getPapersList = paperService.getPapers(wishlistId);
        return getPapersList;
    }
}
