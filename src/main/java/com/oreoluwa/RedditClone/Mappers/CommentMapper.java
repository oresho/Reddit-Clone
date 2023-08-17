package com.oreoluwa.RedditClone.Mappers;

import com.oreoluwa.RedditClone.Dtos.CommentDto;
import com.oreoluwa.RedditClone.Entities.Comment;
import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user" ,source = "user")
    Comment map(CommentDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentDto mapToDto(Comment comment);
}