package com.presenty.backend.service.dto;

import com.presenty.backend.domain.paper.Paper;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class PaperResDto {
    private Long id;
    private Long takerId;
    private Long giverId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long wishlistId;
    private Long itemId;

    public PaperResDto(Paper paper) {
        this.id = paper.getId();
        this.takerId = paper.getTaker().getId();
        this.giverId = paper.getGiver().getId();
        this.content = paper.getContent();
        this.createdAt = paper.getCreatedAt();
        this.updatedAt = paper.getLastModifiedAt();
        this.wishlistId = paper.getWishlist().getId();
        this.itemId = paper.getItem().getId();
    }
}
