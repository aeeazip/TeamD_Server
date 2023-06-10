package com.presenty.backend.service.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ImageDto {

    private Long imageId;

    private String originName;

    private String storedName;

    private String url;
}
