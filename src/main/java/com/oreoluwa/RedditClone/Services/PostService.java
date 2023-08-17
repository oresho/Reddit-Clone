package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Dtos.PostRequest;
import com.oreoluwa.RedditClone.Dtos.PostResponse;

import java.util.List;
public interface PostService {
    PostResponse getPost(Long id);
    List<PostResponse> getAllPosts();
    void save(PostRequest postRequest);
    List<PostResponse> getPostsBySubreddit(Long subredditId);
    List<PostResponse> getPostsByUsername(String username);
}
