package com.presenty.backend.domain.comment.repository;

import com.presenty.backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
