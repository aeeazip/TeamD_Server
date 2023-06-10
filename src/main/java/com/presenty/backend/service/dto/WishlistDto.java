package com.presenty.backend.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistDto {

    private Long id;

    private String title;
}
