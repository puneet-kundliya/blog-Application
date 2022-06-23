package com.blogpost.project.controller;

import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.sql.Timestamp;
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        post.setExcerpt(post.getContent().substring(0,100)+ "...");
        post.setCreatedAt(timestamp);
        post.setPublishedAt(timestamp);
        tag.setCreatedAt(timestamp);
        postService.savePost(post);
        tagService.saveTag(tag);
        return "redirect:/newpost";
    }

    @GetMapping("/")
    public String homePage(Model model){
        List<Posts> listPost = postService.getPost();
        model.addAttribute("postList" , listPost);
        return "allblog";
    }
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") int postId, Model model){
        Optional<Posts> post = postService.getPostById(postId);
        Posts postById = null;

        if(post.isPresent()){
            postById = post.get();
        }

        model.addAttribute("post", postById);
        return "viewblog";
    }
}