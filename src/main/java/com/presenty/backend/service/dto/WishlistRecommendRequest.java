package com.presenty.backend.service.dto;

import com.presenty.backend.service.Choice;
import com.presenty.backend.service.Interest;
import com.presenty.backend.service.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class WishlistRecommendRequest {

    @Size(min = 1, max = 3)
    private List<Interest> interests;

    @Size(min = 1, max = 3)
    private List<Tag> tags;

    @NotNull
    private Choice choice;
}
