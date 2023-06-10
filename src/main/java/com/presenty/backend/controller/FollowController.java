package com.presenty.backend.controller;

import com.presenty.backend.service.FollowService;
import com.presenty.backend.service.dto.FollowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    // 팔로우 신청
    @PutMapping("/{memberId}/to/{followingId}")
    public int applyFollow(@PathVariable Long memberId, @PathVariable Long followingId) {
        return followService.applyFollow(memberId, followingId);
    }

    @GetMapping("/follow/{followerId}")
    public List<FollowDto> getFollowing(@PathVariable Long followerId) {
        List<FollowDto> getFollowingList = followService.getFollowing(followerId);
        return getFollowingList;
    }

}
