package com.presenty.backend.service;

import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.MemberTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long getMemberId(String username) {
        return getMemberByUsername(username).getId();
    }

    public MemberTokenDto getMemberToken(String username) {
        Member member = getMemberByUsername(username);
        return MemberTokenDto.builder()
                .memberId(member.getId())
                .refreshToken(member.getRefreshToken())
                .refreshTokenExpiresAt(member.getRefreshTokenExpiresAt())
                .build();
    }

    @Transactional
    public Member login(String username, String refreshToken, LocalDateTime refreshTokenExpiresAt) {
        Member member = getOrCreate(username);
        member.renewRefreshToken(refreshToken, refreshTokenExpiresAt);
        return member;
    }

    @Transactional
    public void logout(String username) {
        Member member = getMemberByUsername(username);
        member.refreshTokenExpires();
    }

    private Member getOrCreate(String username) {
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        if (memberOptional.isEmpty()) {
            Member member = memberRepository.save(Member.builder()
                    .username(username)
                    .build());
            memberOptional = Optional.of(member);
        }
        return memberOptional.get();
    }

    @Transactional
    public void delete(String username) {
        Member member = getMemberByUsername(username);
        memberRepository.delete(member);
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Account.username=" + username));
    }

}
