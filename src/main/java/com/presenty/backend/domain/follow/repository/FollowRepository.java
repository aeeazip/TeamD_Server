package com.presenty.backend.domain.follow.repository;

import com.presenty.backend.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
