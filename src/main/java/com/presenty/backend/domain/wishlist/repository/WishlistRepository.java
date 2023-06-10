package com.presenty.backend.domain.wishlist.repository;

import com.presenty.backend.domain.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
