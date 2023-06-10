package com.presenty.backend.service.dto;

import com.presenty.backend.domain.follow.Follow;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowDto {
    private Long id;
    private Long followingId;
    private Long followerId;

    @Builder
    public FollowDto(Follow follow){
        this.id = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.followingId = follow.getFollowing().getId();
    }
}
