package com.presenty.backend.domain.paper.repository;

import com.presenty.backend.domain.paper.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {
    @Query(value = "select * from Paper p where p.wishlist_id = ?", nativeQuery = true)
    List<Paper> findByWishlistId(Long wishlistId);
}
