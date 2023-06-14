package com.presenty.backend.service;

import com.presenty.backend.domain.follow.Follow;
import com.presenty.backend.domain.follow.repository.FollowRepository;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.error.exception.DuplicatedFollowingException;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.FollowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public FollowDto applyFollow(Long memberId, Long followingId) {
        Optional<Follow> getFollow = followRepository.checkDuplicated(memberId, followingId);

        if(getFollow.isPresent())
            throw new DuplicatedFollowingException("팔로우 신청에 실패했습니다.");

        Member follower = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member.member_id=" + memberId));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("Member.member_id=" + followingId));

        Follow newFollow = Follow.builder()
                .following(following)
                .follower(follower)
                .build();

        return new FollowDto(followRepository.save(newFollow));
    }

    public List<FollowDto> getFollowing(Long followerId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("Account.member_id=" + followerId));
        List<Follow> followingList = followRepository.findAllByFollower(follower);

        if(followingList.isEmpty())
            throw new EntityNotFoundException("현재 팔로우하고 있는 멤버가 없습니다.");

        return followingList.stream()
                .map(follow -> FollowDto.builder()
                        .id(follow.getId())
                        .followerId(follow.getFollower().getId())
                        .followingId(follow.getFollowing().getId())
                        .build()
                ).toList();
    }
}
