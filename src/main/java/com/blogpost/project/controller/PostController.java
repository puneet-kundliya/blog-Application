package com.blogpost.project.controller;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import com.blogpost.project.service.PostService;
import com.blogpost.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @PostMapping("/updatePost{id}")
    public String updatePost(@PathVariable("id")Integer postId,@ModelAttribute("posts")Posts posts,@ModelAttribute("tag")Tags tag){
        postService.updatePost(posts, tag);
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
//        List<Posts> listPost = postService.getPost();
//        model.addAttribute("postList" , listPost);
        return findPaginated(1,model);
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

    @GetMapping("/post/edit/{id}")
    public String editPost(@PathVariable("id") int postId, Model model) {
        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        Tags tag = new Tags();
        List<Tags> tagsList = postById.getTags();
        String allTags = "";
        for (Tags eachTag : tagsList) {
            String name = eachTag.getName() + ",";
            allTags += name;
        }
        System.out.println(allTags);
        tag.setName(allTags);
        model.addAttribute("post",postById);
        model.addAttribute("tag",tag);
        return "editPost";
    }

    @GetMapping("/searchKeyword")
    public String getByKeyword(@RequestParam("keyword") String keyword, Model model){
        List<Posts> postByKeyword = postService.getPostByKeyword(keyword.toLowerCase());
        model.addAttribute("postList",postByKeyword);
        List<Posts> posts = new ArrayList<>();
        return  "allblog";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") Integer pageNo, Model model){
        Integer pageSize = 10;
        Page<Posts> page = postService.findPaginated(pageNo,pageSize);
        List<Posts> listPost = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("postList", listPost);
        return "allblog";
    }
}