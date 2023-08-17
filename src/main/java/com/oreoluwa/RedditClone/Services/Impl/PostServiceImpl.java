package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.Dtos.PostRequest;
import com.oreoluwa.RedditClone.Dtos.PostResponse;
import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.Subreddit;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.ExceptionHandlers.PostNotFoundException;
import com.oreoluwa.RedditClone.ExceptionHandlers.SubredditNotFoundException;
import com.oreoluwa.RedditClone.Mappers.PostMapper;
import com.oreoluwa.RedditClone.Repositories.PostRepository;
import com.oreoluwa.RedditClone.Repositories.SubredditRepository;
import com.oreoluwa.RedditClone.Repositories.UserRepository;
import com.oreoluwa.RedditClone.Services.AuthenticationService;
import com.oreoluwa.RedditClone.Services.PostService;
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
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
    @Override
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authenticationService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
