package com.presenty.backend.domain.wishlist.repository;

import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByMember(Member member);
}
