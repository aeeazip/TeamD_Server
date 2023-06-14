package com.presenty.backend.service;

import com.presenty.backend.domain.item.repository.ItemRepository;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.domain.paper.Paper;
import com.presenty.backend.domain.paper.repository.PaperRepository;
import com.presenty.backend.domain.wishlist.Wishlist;
import com.presenty.backend.domain.wishlist.repository.WishlistRepository;
import com.presenty.backend.domain.item.Item;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.PaperReqDto;
import com.presenty.backend.service.dto.PaperResDto;
import com.presenty.backend.service.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final PaperRepository paperRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public PaperResDto createPaper(Long wishlistId, Long giverId, PaperReqDto paperReqDto) {
        Member giver = memberRepository.findById(giverId)
                .orElseThrow(() -> new EntityNotFoundException("Account.member_id=" + giverId));
        Member taker = memberRepository.findById(paperReqDto.getTakerId())
                .orElseThrow(() -> new EntityNotFoundException("Account.member_id=" + paperReqDto.getTakerId()));
        Item item = itemRepository.findById(paperReqDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item.item_id=" + paperReqDto.getItemId()));
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist.wishlist_id=" + paperReqDto.getItemId()));

        Paper newPaper = Paper.builder()
                .name(paperReqDto.getName())
                .content(paperReqDto.getContent())
                .item(item)
                .taker(taker)
                .giver(giver)
                .wishlist(wishlist)
                .build();

        return new PaperResDto(paperRepository.save(newPaper));
    }

    public List<PaperResDto> getPapers(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist.wishlist_id=" + wishlistId));
        List<Paper> getPapers = paperRepository.findAllByWishlist(wishlist);

        return getPapers.stream()
                .map(paper -> PaperResDto.builder()
                        .id(paper.getId())
                        .takerId(paper.getTaker().getId())
                        .giverId(paper.getGiver().getId())
                        .content(paper.getContent())
                        .createdAt(paper.getCreatedAt())
                        .updatedAt(paper.getLastModifiedAt())
                        .wishlistId(paper.getWishlist().getId())
                        .itemId(paper.getItem().getId())
                        .build()
                ).toList();
    }
}
