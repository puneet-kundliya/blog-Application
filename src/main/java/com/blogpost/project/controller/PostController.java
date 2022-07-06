package com.blogpost.project.controller;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.service.CustomUserDetailService;
import com.blogpost.project.service.MyUserPrincipal;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @Autowired
    private CustomUserDetailService myUserDetailService;

    @GetMapping("/newpost")
    public String showPage(Model model) {
        Posts posts = new Posts();
        Tags tags = new Tags();
        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        return "newpost";
    }
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("posts") Posts post, @ModelAttribute("tags") Tags tag,
                           @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        post.setPublished(true);
        postService.savePost(post,tag, userPrincipal);
        return "redirect:/";
    }
    @PostMapping("/updatePost{id}")
    public String updatePost(@PathVariable("id")Integer postId,@ModelAttribute("posts")Posts posts,
                             @ModelAttribute("tag")Tags tag){
        postService.updatePost(posts, tag);
        return "redirect:/";
    }
    @PostMapping("/draftPost")
    public String draftPost(@ModelAttribute("posts") Posts post, @ModelAttribute("tags") Tags tag,
                            @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        post.setPublished(false);
        postService.savePost(post,tag, userPrincipal);
        return "redirect:/";
    }
    @GetMapping("/")
    public String homePage(Model model){
        return findPaginated(1,"", "ASC",model ,"");
    }
    @GetMapping("/post{id}")
    public String viewPost(@PathVariable("id") int postId, Model model,
                           @ModelAttribute("comment") Comments comments,
                           @AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        Optional<Posts> post = postService.getPostById(postId);
        Posts postById = post.get();
        List<Comments> commentsList = postById.getComments();
        if(userPrincipal != null){
            model.addAttribute("principalUser", userPrincipal.getUsername());
        }
        model.addAttribute("post", postById);
        model.addAttribute("commentsList", commentsList);
        return "viewblog";
    }
    @GetMapping("/post/edit/{id}")
    public String editPost(@PathVariable("id") int postId, Model model,@AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        if(!postById.getAuthor().equals(userPrincipal.getUsername()) && !userPrincipal.getUsername().equals("Admin")){
            throw new RuntimeException();
        }
        Tags tag = new Tags();
        List<Tags> tagsList = postById.getTags();
        String allTags = tagService.getAllTagName(tagsList);
        tag.setName(allTags);
        model.addAttribute("post",postById);
        model.addAttribute("tag",tag);
        return "editPost";
    }
    @GetMapping("/search")
    public String getByKeyword(@RequestParam("search") String search,
                               @RequestParam("sortDir") String sortDir, Model model){
        String sortField = "";
        return findPaginated(1,sortField,sortDir, model, search.toLowerCase());
    }

    @GetMapping("/filter")
    public String filterByTags(@RequestParam(value = "search",required = false) String search,
                               @RequestParam(value = "sortDir",required = false) String sortDir,Model model,
                               @RequestParam(value = "tagId",required = false,defaultValue = "") List<Integer> IdTags){
//        @RequestParam("sortField") String sortField,
        String sortField = "published_at";
        List<Tags> tagsList = tagService.getAllTag();
        if(IdTags.isEmpty()){
            IdTags = new ArrayList<>();
            for (Tags tag : tagsList ) {
                IdTags.add(tag.getId());
            }
        }
        return  findPaginatedByTags(1,sortField,sortDir,model,IdTags,search);
    }

    @GetMapping("/page/{pageNo}/filter")
    public String findPaginatedByTags(@PathVariable("pageNo") Integer pageNo,
                                      String sortField,
                                      @RequestParam(value = "sortDir",required = false) String sortDir,
                                      Model model, @RequestParam(value = "tagId",required = false, defaultValue = "") List<Integer> IdTags,
                                      @RequestParam(value = "search",defaultValue = "",required = false) String search){
        Integer pageSize = 10;
        sortField = "published_at";
        Page<Posts> page = postService.findPaginatedTags(pageNo,pageSize, IdTags, sortField, sortDir);
        List<Posts> listPost = page.getContent();
        String reqParam = "";
        for (Integer i: IdTags) {
            reqParam += ("&tagId=" + i);
        }
        List<Tags> tagsList = tagService.getAllTag();
        if(IdTags.isEmpty()){
            IdTags = new ArrayList<>();
            for (Tags tag : tagsList ) {
                IdTags.add(tag.getId());
            }
        }
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("postList", listPost);
        model.addAttribute("reqParam", reqParam);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("tagList",tagsList);
        model.addAttribute("filterTags", IdTags);
        return "allblogfilter";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") Integer pageNo,
                                @RequestParam(name = "sortField",required = false) String sortField,
                                 @RequestParam(name = "sortDir",required = false) String sortDir,
                                 Model model,String search){
        Integer pageSize = 10;
        sortField = "published_at";
        Page<Posts> page = postService.findPaginated(pageNo,pageSize, search, sortField, sortDir);
        List<Posts> listPost = page.getContent();
        List<Tags> tagsList = tagService.getAllTag();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("postList", listPost);
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("tagList",tagsList);
        return "allblog";
    }

    @GetMapping("post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId, Principal principal){
        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        if(!postById.getAuthor().equals(principal.getName()) && !principal.getName().equals("Admin")){
            throw new RuntimeException();
        }
        postService.deletePost(postId);
        return "redirect:/";
    }
}