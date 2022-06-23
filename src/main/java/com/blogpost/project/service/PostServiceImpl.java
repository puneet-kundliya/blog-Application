package com.blogpost.project.service;

import com.blogpost.project.model.Posts;
import com.blogpost.project.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Override
    public void savePost(Posts posts) {
        posts.setAuthor("Puneet");
        posts.setPublished(true);
        postRepository.save(posts);
    }
    @Override
    public List<Posts> getPost() {
        return postRepository.findAll();
    }
    public Optional<Posts> getPostById(Integer postId){
       Optional<Posts> post =  postRepository.findById(postId);
       return post;
    }
}
