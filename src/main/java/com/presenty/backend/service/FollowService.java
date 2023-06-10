package com.presenty.backend.service;

import com.presenty.backend.domain.follow.Follow;
import com.presenty.backend.domain.follow.repository.FollowRepository;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.error.exception.DuplicateFollowingException;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.FollowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    public int applyFollow(Long memberId, Long followingId) {
        Optional<Follow> getFollow = followRepository.checkDuplicated(memberId, followingId);

        if(getFollow.isPresent())
            throw new DuplicateFollowingException("팔로우 신청에 실패했습니다.");

        Member follower = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member.member_id=" + memberId));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("Member.member_id=" + followingId));

        Follow newFollow = Follow.builder()
                .following(following)
                .follower(follower)
                .build();

        followRepository.save(newFollow);
        return 1;
    }

    public List<FollowDto> getFollowing(Long followerId) {
        Optional<List<Follow>> followingList = followRepository.findByFollowerId(followerId);

        if(!followingList.isPresent())
            throw new EntityNotFoundException("현재 팔로우하고 있는 멤버가 없습니다.");

        List<FollowDto> followDtoList = new ArrayList<FollowDto>();
        for(Follow f : followingList.get()) {
            followDtoList.add(new FollowDto(f));
        }
        return followDtoList;
    }
}
