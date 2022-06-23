package com.blogpost.project.service;

import com.blogpost.project.model.Posts;
import java.util.List;
import java.util.Optional;

public interface PostService {
    void savePost(Posts post);
    List<Posts> getPost();
    Optional<Posts> getPostById(Integer postId);
}
