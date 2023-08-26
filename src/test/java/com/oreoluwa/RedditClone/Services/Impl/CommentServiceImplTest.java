package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.ExceptionHandlers.SpringRedditException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceImplTest {

    @Test
    @DisplayName("This Test Should Pass if Comment Does Not Contain profanity")
    void shouldNotContainSwearWordsInsideComment() {
        CommentServiceImpl commentService = new CommentServiceImpl
                (null,null,null,null
                        ,null,null);
//        Assertions.assertFalse(commentService.containsSwearWords("This is a clean comment"));
        assertThat(commentService.containsSwearWords("This is a clean comment")).isFalse();
    }

    @Test
    @DisplayName("This Test Should Throw an Exception if Comment Contains profanity")
    void shouldFailWhenCommentContainsSwearWords() {
        CommentServiceImpl commentService = new CommentServiceImpl
                (null,null,null,null
                        ,null,null);
//        SpringRedditException exception = assertThrows(SpringRedditException.class, () ->
//            commentService.containsSwearWords("This is a shitty comment"));
//        Assertions.assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));
        assertThatThrownBy(() -> commentService.containsSwearWords("This is a shitty comment"))
                .isInstanceOf(SpringRedditException.class).hasMessage("Comments contains unacceptable language");
    }
}