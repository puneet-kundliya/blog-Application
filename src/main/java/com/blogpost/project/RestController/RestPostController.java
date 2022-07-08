package com.blogpost.project.RestController;

import com.blogpost.project.Exception.CustomException;
import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.model.Users;
import com.blogpost.project.repository.UserRepository;
import com.blogpost.project.service.CustomUserDetailService;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestPostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @Autowired
    private CustomUserDetailService myUserDetailService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/posts/save")
    public void savePost(@RequestBody Posts post,
                         @AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        Tags tag = null;
        postService.savePost(post,tag, userPrincipal);
    }

    @GetMapping("/posts/{id}")
    public Posts viewPost(@PathVariable("id") int postId,
                          @ModelAttribute("comment") Comments comments,
                          @AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        try {
            Optional<Posts> post = postService.getPostById(postId);
            Posts postById = post.get();
            return  postById;
        }catch (NullPointerException e){
            throw new CustomException("Post id Invalid");
        }
    }

    @PutMapping("/posts/{id}")
    public String updatePost(@PathVariable("id")Integer postId,@RequestBody Posts posts,
                             @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        Optional<Posts> postById = postService.getPostById(postId);
        try {
            Posts post = postById.get();
            Users user = userRepository.findUserByName(userPrincipal.getUsername()).get();
            if(!postById.get().getAuthor().equals(userPrincipal.getUsername()) && !user.getRoles().equals("ROLE_ADMIN")){
                throw new CustomException("You don't have Access to Update Post");
            }
            Tags tag = null;
            postService.updatePost(posts,tag,postId);
            return "Post Updated";
        }
        catch(NullPointerException e){
            throw new CustomException("Post Id Invalid");
        }
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId,
                             @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        try{
            Optional<Posts> postById = postService.getPostById(postId);
            Users user = userRepository.findUserByName(userPrincipal.getUsername()).get();
            if(!postById.get().getAuthor().equals(userPrincipal.getUsername()) && !user.getRoles().equals("ROLE_ADMIN")) {
                throw new CustomException("You are not Authorised to Delete");
            }
            postService.deletePost(postId);
            return "Post deleted";
        }
        catch (NullPointerException e){
            throw new CustomException("User Not Present or Invalid Post Id");
        }
    }

    @GetMapping("/posts")
    public List<Posts> findPaginated(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "sortField",required = false, defaultValue = "published_at") String sortField,
                                     @RequestParam(name = "order" ,required = false, defaultValue = "desc") String order,
                                     @RequestParam(name = "search",required = false, defaultValue = "") String search,
                                     @RequestParam(value = "tagId",required = false, defaultValue = "") List<Integer> idTags){
        Integer pageSize = 10;
        List<Tags> tagsList = tagService.getAllTag();
        idTags = new ArrayList<>();
        for (Tags tag : tagsList ) {
            idTags.add(tag.getId());
        }
        Page<Posts> pageTags = postService.findPaginatedTags(pageNo,pageSize, idTags, sortField, order);
        List<Posts> postTags = pageTags.getContent();
        Page<Posts> pageSearch = postService.findPaginated(pageNo,pageSize, search, sortField, order);
        List<Posts> postSearch = pageSearch.getContent();
        List<Posts>listPost = new ArrayList<>();
        for (Posts post: pageTags) {
            if(postSearch.contains(post)){
                listPost.add(post);
            }
        }
        return listPost;
    }
}













