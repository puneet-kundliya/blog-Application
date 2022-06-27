package com.blogpost.project.controller;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.service.CommentService;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @GetMapping("/newpost")
    public String showPage(Model model) {
        Posts posts = new Posts();
        Tags tags = new Tags();
        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        return "newpost";
    }
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("posts") Posts post, @ModelAttribute("tags") Tags tag){
        post.setPublished(true);
        postService.savePost(post,tag);
        return "redirect:/";
    }

    @PostMapping("/draftPost")
    public String draftPost(@ModelAttribute("posts") Posts post, @ModelAttribute("tags") Tags tag){
        post.setPublished(false);
        postService.savePost(post,tag);
        return "redirect:/";
    }
    @GetMapping("/")
    public String homePage(Model model){
        List<Posts> listPost = postService.getPost();
        model.addAttribute("postList" , listPost);
        return "allblog";
    }
    @GetMapping("/post{id}")
    public String viewPost(@PathVariable("id") int postId, Model model, @ModelAttribute("comment") Comments comments) {
        Optional<Posts> post = postService.getPostById(postId);
        Posts postById = post.get();
        List<Comments> commentsList = postById.getComments();
        model.addAttribute("post", postById);
        model.addAttribute("commentsList", commentsList);
        return "viewblog";
    }

    @PostMapping("/updatePost{id}")
    public String updatePost(@PathVariable("id")Integer postId,@ModelAttribute("posts")Posts posts){
        posts.setPublished(true);
        Optional<Posts> postById = postService.getPostById(postId);
        Posts postToUpdate = postById.get();
        List<Comments> oldComments = postToUpdate.getComments();
        posts.getComments().addAll(oldComments);
        posts.setCreatedAt(postToUpdate.getCreatedAt());
        postService.updatePost(posts);
        return "redirect:/";
    }
    @GetMapping("/post/edit/{id}")
    public String editPost(@PathVariable("id") int postId, Model model) {
        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        Tags tag = new Tags();
        model.addAttribute(postById);
        model.addAttribute(tag);
        return "editPost";
    }

    @GetMapping("/searchKeyword")
    public String getByKeyword(@RequestParam("keyword") String keyword, Model model){
        System.out.println(keyword);
        List<Posts> postByKeyword = postService.getPostByKeyword(keyword);
        System.out.println(postByKeyword.get(0).getContent());
        model.addAttribute("postList",postByKeyword);
        return  "allblog";
    }
}