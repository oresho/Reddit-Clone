package com.oreoluwa.RedditClone.ExceptionHandlers;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message) {
        super(message);
    }
}