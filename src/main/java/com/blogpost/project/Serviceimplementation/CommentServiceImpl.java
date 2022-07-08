package com.blogpost.project.Serviceimplementation;

import com.blogpost.project.model.Comments;
import com.blogpost.project.repository.CommentRepository;
import com.blogpost.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public void saveComment(Comments comments, MyUserPrincipal userPrincipal) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        comments.setCreatedAt(timestamp);
    }
    @Override
    public void deleteComment(Comments comment) {
        commentRepository.delete(comment);
    }
    public Comments getCommentById(Integer commentId) {
        Optional<Comments> commentById = commentRepository.findCommentById(commentId);
        Comments oldComment = new Comments();
        if (commentById.isPresent()) {
            oldComment = commentById.get();
            return oldComment;
        } else {
            throw new NullPointerException("Comment not Found or Comment Id Invalid");
        }
    }
    public void updateComments(Comments comment, Comments oldComment) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        oldComment.setComment(comment.getComment());
        oldComment.setUpdatedAt(timestamp);
        commentRepository.save(oldComment);
    }
}
