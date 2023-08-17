package com.oreoluwa.RedditClone.ExceptionHandlers;


public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String message) {

        super(message + " does not exist");
    }
}
