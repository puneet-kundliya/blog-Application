package com.blogpost.project.service;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.PostRepository;
import com.blogpost.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    TagService tagService;


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
    public void updatePost(Posts posts, Tags tag) {
        posts.setPublished(true);
        Optional<Posts> postById = getPostById(posts.getId());
        Posts postToUpdate = postById.get();
        List<Comments> oldComments = postToUpdate.getComments();
        posts.getComments().addAll(oldComments);
        posts.setCreatedAt(postToUpdate.getCreatedAt());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        posts.setPublishedAt(timestamp);
        posts.setAuthor("Puneet");
        if(posts.getContent().length() <=100){
            posts.setExcerpt(posts.getContent() + "....");
        }
        else{
            posts.setExcerpt(posts.getContent().substring(0,100)+ "....");
        }
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
    public void savePostComments(Posts posts) {
        postRepository.save(posts);
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

    @Override
    public Page<Posts> findPaginated(Integer pageNo, Integer pageSize, String keyword,  String sortField, String sortDirection) {

        Sort sort;
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = Sort.by(Sort.Direction.ASC,"published_at");
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, "published_at");
        }
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);
        return this.postRepository.findBySearch(keyword, pageable);
    }

    @Override
    public Page<Posts> findPaginatedTags(Integer pageNo, Integer pageSize, List<Integer> idTags, String sortField, String sortDirection) {

        Sort sort;
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = Sort.by(Sort.Direction.ASC,"published_at");
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, "published_at");
        }
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);
        return this.postRepository.findByTags(idTags,pageable);
    }

    @Override
    public List<Posts> getPostByTags(List<Integer> idTags) throws Exception {
        List<Posts> postByTagId = new ArrayList<>();

        for (Integer tagId: idTags ) {
            Optional<Tags> tag = tagService.getTagById(tagId);
            for (Posts post: tag.get().getPosts()) {
                if(!postByTagId.contains(post)){
                    postByTagId.add(post);
                }
            }
        }
        return postByTagId;
    }
}
