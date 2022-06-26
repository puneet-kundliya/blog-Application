package com.blogpost.project.service;

import com.blogpost.project.model.Comments;
import com.blogpost.project.model.Posts;
import com.blogpost.project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void saveComment(Comments comments) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        comments.setCreatedAt(timestamp);
    }

    @Override
    public void deleteComment(Comments comment) {
        commentRepository.delete(comment);
    }

    public Comments getCommentById(Integer commentId){
        List<Comments> listComment = commentRepository.findAll();
        for (Comments comment: listComment) {
            if(comment.getId() == commentId){
                return comment;
            }
        }
        return null;
    }



















    public void updateComments(Comments comment, Comments oldComment) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        oldComment.setComment(comment.getComment());
        oldComment.setUpdatedAt(timestamp);
        commentRepository.save(oldComment);
    }
}
