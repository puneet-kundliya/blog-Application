package com.blogpost.project.service;

import com.blogpost.project.model.Comments;

public interface CommentService {
    void saveComment(Comments comments);
    void deleteComment(Comments comment);
    Comments getCommentById(Integer commentId);
    void  updateComments(Comments comment, Comments oldComment);
}
