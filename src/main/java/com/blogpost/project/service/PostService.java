package com.blogpost.project.service;

import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void savePost(Posts post,Tags tag);

    void savePostComments(Posts posts);

    List<Posts> getPost();
    Optional<Posts> getPostById(Integer postId);

    void updatePost(Posts posts, Tags tags);

    void getCommentById(Integer id);

    List<Posts> getPostByKeyword(String keyword);

    Page<Posts> findPaginated(Integer pageNo, Integer pageSize);

}
