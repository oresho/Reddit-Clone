package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Dtos.SubredditDto;
import com.oreoluwa.RedditClone.Entities.Subreddit;
import com.oreoluwa.RedditClone.ExceptionHandlers.SubredditNotFoundException;

import java.util.List;

public interface SubredditService {
    List<SubredditDto> getAll();
    SubredditDto save(SubredditDto subredditDto);
    SubredditDto getSubreddit(Long id) throws SubredditNotFoundException;
//    SubredditDto mapToDto(Subreddit subreddit);
//    Subreddit mapToSubreddit(SubredditDto subredditDto);
}
