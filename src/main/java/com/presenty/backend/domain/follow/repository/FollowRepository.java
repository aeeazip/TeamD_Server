package com.presenty.backend.domain.follow.repository;

import com.presenty.backend.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "select * from follow f where f.follower_id = ? and f.following_id = ?", nativeQuery = true)
    Optional<Follow> checkDuplicated(Long memberId, Long followingId);

    @Query(value = "select * from follow f where f.follower_id = ?", nativeQuery = true)
    Optional<List<Follow>> findByFollowerId(Long followerId);
}
