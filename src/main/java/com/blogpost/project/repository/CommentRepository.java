package com.blogpost.project.repository;

import com.blogpost.project.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Integer> {
    Optional<Comments> findCommentById(Integer integer);
}
