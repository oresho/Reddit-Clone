package com.oreoluwa.RedditClone.Entities;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    VoteType(int direction) {
    }
}
