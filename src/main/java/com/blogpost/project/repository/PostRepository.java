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

    @Query(value = "select * from public.posts post where lower(post.title) like %?1% OR lower(post.content) LIke %?1% OR post.id in (select id from post_tags inner Join tags on tags.id = post_tags.tag_id where lower(tags.name) like %?1%)",nativeQuery = true)
    Page<Posts> findBySearch(String keyword, Pageable pageable);

    @Query(value = "select * from posts where id in (select p.id from posts p join post_tags pt on p.id = pt.post_id join tags t on t.id = pt.tag_id where t.id in :selectedTags)", nativeQuery = true)
    Page<Posts> findByTags(@Param("selectedTags") List<Integer> selectedTags, Pageable pageable);
}
