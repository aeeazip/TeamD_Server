package com.presenty.backend.domain.paper.repository;

import com.presenty.backend.domain.paper.Paper;
import com.presenty.backend.domain.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findAllByWishlist(Wishlist wishlist);
}
