package com.oreoluwa.RedditClone.ExceptionHandlers;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String string) {
        super(string + " not found");
    }
}
