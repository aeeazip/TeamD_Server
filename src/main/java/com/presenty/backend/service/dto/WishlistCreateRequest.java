package com.presenty.backend.service.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WishlistCreateRequest {

    @Size(min = 5, max = 25)
    private String title;

}
