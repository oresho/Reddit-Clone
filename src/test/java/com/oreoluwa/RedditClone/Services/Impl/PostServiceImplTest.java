package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.Dtos.PostRequest;
import com.oreoluwa.RedditClone.Dtos.PostResponse;
import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.Role;
import com.oreoluwa.RedditClone.Entities.Subreddit;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.Mappers.PostMapper;
import com.oreoluwa.RedditClone.Repositories.PostRepository;
import com.oreoluwa.RedditClone.Repositories.SubredditRepository;
import com.oreoluwa.RedditClone.Repositories.UserRepository;
import com.oreoluwa.RedditClone.Services.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private SubredditRepository subredditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationService authService;
    @Mock
    private PostMapper postMapper;
    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;
    private PostServiceImpl postService;
    @BeforeEach
    public void setup() {
        postService = new PostServiceImpl(postRepository,subredditRepository,userRepository,authService,postMapper);
    }

    @Test
    @DisplayName("Should Find Post By Id")
    void shouldFindPostById() {
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test", "Test User", "Test Subredit", 0, 0, "1 Hour Ago");

        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post)); // these two lines are mocked versions of what happens in postService
        // when postRepo is called we expect the new post object we created to be returned
        // when it's time to map to dto we expect our expectedPostResponse to be returned mockito helps do that.
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostResponse actualPostResponse = postService.getPost(123L);

        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should Save Posts")
    public void shouldSavePosts() {
        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true, Role.USER);
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

        Mockito.when(subredditRepository.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        Mockito.when(authService.getCurrentUser())
                .thenReturn(currentUser);
        Mockito.when(postMapper.map(eq(postRequest), eq(subreddit), eq(currentUser)))
                .thenReturn(post);
        Mockito.when(postRepository.save(post)).thenReturn(post);
        postService.save(postRequest);
        //Mockito.verify(postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
    }
}