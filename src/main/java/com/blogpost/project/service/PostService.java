package com.blogpost.project.service;

import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface PostService {
    void savePost(Posts post, Tags tag, MyUserPrincipal userPrincipal);

    void savePostComments(Posts posts);

    Optional<Posts> getPostById(Integer postId);

    void updatePost(Posts posts, Tags tags, Integer postId);

    Page<Posts> findPaginated(Integer pageNo, Integer pageSize, String keyword, String sortDirection, String sortDir);

    List<Posts> getPostByTags(List<Integer> idTags) throws Exception;

    Page<Posts> findPaginatedTags(Integer pageNo, Integer pageSize, List<Integer> idTags, String sortField, String sortDir);

    void deletePost(Integer postId);

    Page<Posts> findPaginatedSearchTags(Integer pageNo, Integer pageSize, List<Integer> idTags, String sortField, String sortDir,String keyword);

}
