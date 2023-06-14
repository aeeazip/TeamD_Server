package com.presenty.backend.service.dto;

import com.presenty.backend.domain.follow.Follow;
import lombok.*;

@Data
@Builder
public class FollowDto {
    private Long id;
    private Long followingId;
    private Long followerId;

    public FollowDto(Follow follow){
        this.id = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.followingId = follow.getFollowing().getId();
    }
}
