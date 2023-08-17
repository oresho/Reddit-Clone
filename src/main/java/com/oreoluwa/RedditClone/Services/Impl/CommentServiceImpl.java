package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.Dtos.CommentDto;
import com.oreoluwa.RedditClone.Entities.Comment;
import com.oreoluwa.RedditClone.Entities.NotificationEmail;
import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.ExceptionHandlers.PostNotFoundException;
import com.oreoluwa.RedditClone.Mappers.CommentMapper;
import com.oreoluwa.RedditClone.Repositories.CommentRepository;
import com.oreoluwa.RedditClone.Repositories.PostRepository;
import com.oreoluwa.RedditClone.Repositories.UserRepository;
import com.oreoluwa.RedditClone.Services.AuthenticationService;
import com.oreoluwa.RedditClone.Services.CommentService;
import com.oreoluwa.RedditClone.Services.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final String POST_URL = "http://localhost:8080/api/v1/posts/";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final MailService mailService;

    @Override
    public void createComment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentDto, post, authenticationService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailService.buildMail(
                post.getUser().getUsername() + " posted a comment on your post." + POST_URL + post.getPostId());
        sendCommentNotification(message, post.getUser());
    }

    @Override
    public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public List<CommentDto> getCommentsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                user.getUsername() + " Commented on your post",
                user.getEmail(), message));
    }
}
