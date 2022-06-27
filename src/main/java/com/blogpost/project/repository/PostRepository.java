package com.blogpost.project.repository;

import com.blogpost.project.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
    @Query(value = "SELECT * FROM public.posts post WHERE LOWER(post.title) LIKE  %?1%  OR LOWER(post.content) LIKE %?1%",nativeQuery = true)
    Page<Posts> findBySearch(String keyword, Pageable pageable);
}
