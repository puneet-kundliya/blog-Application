package com.blogpost.project.repository;

import com.blogpost.project.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {

    @Query(value = "SELECT * FROM public.posts post WHERE post.title LIKE  %:keyword%  OR post.content LIKE %:keyword%",nativeQuery = true)
    List<Posts> findPostBySearch(@Param("keyword") String keyword);
}
