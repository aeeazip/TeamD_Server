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

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final WishlistRepository wishlistRepository;

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

    public int deleteItem(Long wishlistId, Long itemId) {
        Item getItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item.item_id=" + itemId));

        if(getItem.getOneoff() == 1)
            getItem.update(0);
        else
            getItem.update(getItem.getOneoff()-1);

        return 1;
    }
}
