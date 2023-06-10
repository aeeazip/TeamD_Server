package com.presenty.backend.service;

import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.domain.wishlist.Wishlist;
import com.presenty.backend.domain.wishlist.repository.WishlistRepository;
import com.presenty.backend.service.dto.CreateResult;
import com.presenty.backend.service.dto.WishlistCreateRequest;
import com.presenty.backend.service.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public CreateResult<Long> create(WishlistCreateRequest request, Long memberId) {
        Wishlist wishlist = wishlistRepository.save(Wishlist.builder()
                .title(request.getTitle())
                .member(memberRepository.getReferenceById(memberId))
                .build());
        return new CreateResult<>(wishlist.getId());
    }

    public List<WishlistDto> getWishlists(Long memberId) {
        return wishlistRepository.findAllByMember(memberRepository.getReferenceById(memberId))
                .stream()
                .map(item -> WishlistDto.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .build()
                ).toList();
    }
}
