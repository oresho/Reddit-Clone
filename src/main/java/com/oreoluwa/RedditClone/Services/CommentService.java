package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Dtos.CommentDto;
import com.oreoluwa.RedditClone.Entities.User;

import java.util.List;

public interface CommentService {
    void createComment(CommentDto commentDto);
    List<CommentDto> getCommentsByPost(Long postId);
    List<CommentDto> getCommentsByUser(String username);
}
