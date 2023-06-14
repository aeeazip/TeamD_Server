package com.presenty.backend.service;

import com.presenty.backend.domain.item.Item;
import com.presenty.backend.domain.item.repository.ItemRepository;
import com.presenty.backend.domain.wishlist.Wishlist;
import com.presenty.backend.domain.wishlist.repository.WishlistRepository;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.ItemReqDto;
import com.presenty.backend.service.dto.ItemResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public ItemResDto addItem(Long wishlistId, ItemReqDto itemReqDto) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist.wishlist_id=" + wishlistId));

        Item item = Item.builder()
                .name(itemReqDto.getName())
                .oneoff(itemReqDto.getOneoff())
                .wishlist(wishlist)
                .build();

        return new ItemResDto(itemRepository.save(item));
    }

    // 선물 받아서 위시리스트에서 개수 감소
    @Transactional
    public ItemResDto updateItemOneoff(Long wishlistId, Long itemId) {
        Item getItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item.item_id=" + itemId));

        // 일회성 선물인 경우 : oneoff 1 -> 0 (reception : true 의미)
        // 다회성 선물인 경우 : oneoff 1 감소 (reception : false 의미)
        if (getItem.getOneoff() == 1)
            getItem.update(0);
        else if (getItem.getOneoff() > 1)
            getItem.update(getItem.getOneoff() - 1);

        return new ItemResDto(itemRepository.save(getItem));
    }

    // taker가 위시리스트에서 상품 완전 삭제
    @Transactional
    public String deleteItem(Long wishlistId, Long itemId) {
        Item getItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item.item_id=" + itemId));
        itemRepository.deleteById(itemId);
        return getItem.getName() + "가 삭제되었습니다.";
    }
}
