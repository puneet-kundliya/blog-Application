package com.blogpost.project.service;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.PostRepository;
import com.blogpost.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;
    @Override
    public void savePost(Posts posts,Tags tag) {
        posts.setAuthor("Puneet");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(posts.getContent().length() <=100){
            posts.setExcerpt(posts.getContent() + "....");
        }
        else{
            posts.setExcerpt(posts.getContent().substring(0,100)+ "....");
        }
        posts.setCreatedAt(timestamp);
        posts.setPublishedAt(timestamp);
        String tagName = tag.getName();
        String[] array = tagName.split(",");
        for (String name : array){
            tag.getPosts().add(posts);
            Tags tagsDb = tagRepository.getTagByName(name);
            if(tagsDb == null){
                Tags newTag = new Tags();
                newTag.setName(name);
                newTag.setCreatedAt(timestamp);
                posts.getTags().add(newTag);
                tagRepository.save(newTag);
            }
            else{
                posts.getTags().add(tagsDb);
            }
        }
        postRepository.save(posts);
    }

    @Override
    public void updatePost(Posts posts) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        posts.setPublishedAt(timestamp);
        posts.setAuthor("Puneet");
        if(posts.getContent().length() <=100){
            posts.setExcerpt(posts.getContent() + "....");
        }
        else{
            posts.setExcerpt(posts.getContent().substring(0,100)+ "....");
        }
        postRepository.save(posts);
    }

    @Override
    public void getCommentById(Integer id) {

    }

    @Override
    public List<Posts> getPostByKeyword(String keyword) {
        List<Posts> posts = postRepository.findPostBySearch(keyword);
//        System.out.println(posts.get(0).getContent());
        return posts;
    }

    @Override
    public void savePostComments(Posts posts) {
        postRepository.save(posts);
    }

    @Override
    public List<Posts> getPost() {
        return postRepository.findAll();
    }
    public Optional<Posts> getPostById(Integer postId){
       Optional<Posts> post=  postRepository.findById(postId);
       if(post.isPresent()){
           return post;
       }
       else{
           throw new RuntimeException("Post is not present");
       }
    }
}
