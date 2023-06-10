package com.presenty.backend.domain.paper.repository;

import com.presenty.backend.domain.paper.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Long> {
}
