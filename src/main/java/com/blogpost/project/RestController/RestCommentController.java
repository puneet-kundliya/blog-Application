package com.blogpost.project.RestController;

import com.blogpost.project.Exception.CustomException;
import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Users;
import com.blogpost.project.repository.UserRepository;
import com.blogpost.project.service.CommentService;
import com.blogpost.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class RestCommentController {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/{postId}/comments")
    public String saveComments(@PathVariable("postId") Integer postId, @RequestBody Comments comments,
                             @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        if(userPrincipal != null){
            comments.setName(userPrincipal.getUsername());
            comments.setEmail(userPrincipal.getEmail());
        }
        try{
            Optional<Posts> postById = postService.getPostById(postId);
            Posts posts = postById.get();
            comments.setPostId(postId);
            commentService.saveComment(comments, userPrincipal);
            posts.getComments().add(comments);
            postService.savePostComments(posts);
        }
        catch (Exception exception){
            return "Post ID invalid";
        }
        return "Comment Saved";
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public String deleteComments(@PathVariable("commentId")Integer commentId, @PathVariable("postId") Integer postId,
                                 @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        try{
            Users user = userRepository.findUserByName(userPrincipal.getUsername()).get();
            Optional<Posts> postById = postService.getPostById(postId);
            Comments comment = commentService.getCommentById(commentId);
            if(!comment.getPostId().equals(postId)){
                throw new CustomException("Comment is not present in this Post");
            }
            if(!postById.get().getAuthor().equals(userPrincipal.getUsername()) && !user.getRoles().equals("ROLE_ADMIN")){
                throw new CustomException("You don't have Access to Delete Comment");
            }
            Comments comments = commentService.getCommentById(commentId);
            commentService.deleteComment(comments);
            return "Comment Deleted";
        }catch (NullPointerException e){
            return "Comment Id or PostId is Invalid";
        }
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public String updateComment(@PathVariable("commentId")Integer commentId, @PathVariable("postId") Integer postId,
                              @RequestBody Comments comments, @AuthenticationPrincipal MyUserPrincipal userPrincipal){
        try{
            Users user = userRepository.findUserByName(userPrincipal.getUsername()).get();
            Optional<Posts> postById = postService.getPostById(postId);
            Comments comment = commentService.getCommentById(commentId);
            if(!comment.getPostId().equals(postId)){
                throw new CustomException("Comment is not present in this Post") ;
            }
            if(!postById.get().getAuthor().equals(userPrincipal.getUsername()) && !user.getRoles().equals("ROLE_ADMIN")){
                throw new CustomException("You don't have Access to Update Comment");
            }
            Comments oldComments = commentService.getCommentById(commentId);
            commentService.updateComments(comments,oldComments);
            return "Comment Updated";
        }
        catch (Exception e){
            return "Comment Id or PostId is Invalid";
        }
    }
}