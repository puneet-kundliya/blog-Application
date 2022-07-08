package com.blogpost.project.controller;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.service.CommentService;
import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @PostMapping("/saveComment{id}")
    public String saveComments(@PathVariable("id") Integer id, Comments comments, @AuthenticationPrincipal MyUserPrincipal userPrincipal){

        if(userPrincipal != null){
            comments.setName(userPrincipal.getUsername());
            comments.setEmail(userPrincipal.getEmail());
        }
        Optional<Posts> postById = postService.getPostById(id);
        Posts posts = postById.get();
        comments.setPostId(id);
        commentService.saveComment(comments, userPrincipal);
        posts.getComments().add(comments);
        postService.savePostComments(posts);
        return "redirect:/post{id}";
    }
    @GetMapping("/post{postId}/deleteComment{commentId}")
    public String deleteComments(@PathVariable("postId") Integer postId, @PathVariable("commentId")Integer commentId,
                                 @AuthenticationPrincipal MyUserPrincipal userPrincipal){

        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        if(!postById.getAuthor().equals(userPrincipal.getUsername()) && !userPrincipal.getUsername().equals("Admin")){
            throw new RuntimeException();
        }
        Comments comments = commentService.getCommentById(commentId);
        commentService.deleteComment(comments);
        return "redirect:/post{postId}";
    }
    @GetMapping("/post{postId}/viewEditComment{commentId}")
    public String viewEditComment(@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId, Model model, Principal principal){
        Optional<Posts> postsOptional = postService.getPostById(postId);
        Posts postById = postsOptional.get();
        if(!postById.getAuthor().equals(principal.getName()) && !principal.getName().equals("Admin")){
            throw new RuntimeException();
        }
        Comments comments = commentService.getCommentById(commentId);
        model.addAttribute("comments", comments);
        return "editComment";
    }
    @PostMapping("/post{postId}/editComment{commentId}")
    public String editComment(@PathVariable("commentId")Integer commentId,@ModelAttribute("comments") Comments comments){
        Comments oldComments = commentService.getCommentById(commentId);
        commentService.updateComments(comments,oldComments);
        return "redirect:/post{postId}";
    }
}