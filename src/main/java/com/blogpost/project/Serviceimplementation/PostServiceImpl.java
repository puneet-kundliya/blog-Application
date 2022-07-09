package com.blogpost.project.Serviceimplementation;

import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.PostRepository;
import com.blogpost.project.repository.TagRepository;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void savePost(Posts posts, Tags tag, MyUserPrincipal userPrincipal) {
        posts.setAuthor(userPrincipal.getUsername());
        posts.setPublished(true);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(posts.getContent().length() <=200){
            posts.setExcerpt(posts.getContent() + "....");
        }
        else{
            posts.setExcerpt(posts.getContent().substring(0,200)+ "....");
        }
        posts.setCreatedAt(timestamp);
        posts.setPublishedAt(timestamp);
        List<String> array = new ArrayList<>();
        if(tag == null){
            array = Arrays.asList(posts.getTagString().split(","));
        }
        else{
            System.out.println("hello");
            String tagName = tag.getName();
            array = Arrays.asList(tagName.split(","));
        }
        for (String name : array){
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
    public void updatePost(Posts posts, Tags tag, Integer postId) {
        Optional<Posts> postById = getPostById(postId);
        Posts postToUpdate;
        if(postById.isPresent()){
            postToUpdate = postById.get();
        }
        else {
            throw new RuntimeException();
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        postToUpdate.setUpdatedAt(timestamp);
        postToUpdate.setTitle(posts.getTitle());
        if(posts.getContent().length() <=100){
            postToUpdate.setExcerpt(posts.getContent() + "....");
        }
        else{
            postToUpdate.setExcerpt(posts.getContent().substring(0,100)+ "....");
        }
        String[] array;
        if(tag == null){
            array = posts.getTagString().split(",");
        }
        else{
            String tagName = tag.getName();
            array = tagName.split(",");
        }
        for (String tagName : array) {
            Tags tagsDb = tagRepository.getTagByName(tagName);
            if (tagsDb == null) {
                Tags newTag = new Tags();
                newTag.setName(tagName);
                newTag.setCreatedAt(timestamp);
                tagService.save(newTag);
                posts.getTags().add(newTag);
            }else {
                posts.getTags().add(tagsDb);
            }
        }
        postToUpdate.setTags(posts.getTags());
        postRepository.save(postToUpdate);
    }

    @Override
    public void savePostComments(Posts posts) {
        postRepository.save(posts);
    }

    public Optional<Posts> getPostById(Integer postId){
       Optional<Posts> post=  postRepository.findById(postId);
        if (post.isPresent()) {
            return post;
        } else {
            throw new NullPointerException("Did not find post " + postId);
        }
    }

    @Override
    public void deletePost(Integer postId) {
        Posts post = getPostById(postId).get();
        postRepository.delete(post);
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
    public Page<Posts> findPaginatedSearchTags(Integer pageNo, Integer pageSize, List<Integer> idTags, String sortField, String sortDir, String keyword) {
        Sort sort;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(Sort.Direction.ASC,"published_at");
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, "published_at");
        }
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);

        return this.postRepository.findByTagSearch(pageable, keyword ,idTags);
    }
}
