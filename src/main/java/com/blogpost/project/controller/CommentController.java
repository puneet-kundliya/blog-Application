package com.blogpost.project.controller;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.service.CommentService;
import com.blogpost.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @PostMapping("/saveComment{id}")
    public String saveComments(@PathVariable("id") Integer id, Comments comments){
        Optional<Posts> postById = postService.getPostById(id);
        Posts posts = postById.get();
        comments.setPostId(id);
        commentService.saveComment(comments);
        posts.getComments().add(comments);
        postService.savePostComments(posts);
        return "redirect:/post{id}";
    }
    @GetMapping("/post{postId}/deleteComment{commentId}")
    public String deleteComments(@PathVariable("postId") Integer postId, @PathVariable("commentId")Integer commentId){
        Comments comments = commentService.getCommentById(commentId);
        commentService.deleteComment(comments);
        return "redirect:/post{postId}";
    }
    @GetMapping("/post{postId}/viewEditComment{commentId}")
    public String viewEditComment(@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId, Model model){
        Comments comments = commentService.getCommentById(commentId);
        model.addAttribute("comments", comments);
        return "editComment";
    }
    @PostMapping("/post{postId}/editComment{commentId}")
    public String editComment(@PathVariable("commentId")Integer commentId,@ModelAttribute("comments") Comments comments){
        Comments oldComments = commentService.getCommentById(commentId);
        System.out.println(oldComments.toString());
        System.out.println(comments.getComment());
        commentService.updateComments(comments,oldComments);
        return "redirect:/post{postId}";
    }
}