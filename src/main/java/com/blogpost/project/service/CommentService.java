package com.blogpost.project.service;

import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.model.Comments;

public interface CommentService {
    void saveComment(Comments comments, MyUserPrincipal userPrincipal);
    void deleteComment(Comments comment);
    Comments getCommentById(Integer commentId);
    void  updateComments(Comments comment, Comments oldComment);
}
