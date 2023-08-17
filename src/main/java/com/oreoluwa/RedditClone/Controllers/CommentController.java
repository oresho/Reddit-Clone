package com.oreoluwa.RedditClone.Controllers;

import com.oreoluwa.RedditClone.Dtos.CommentDto;
import com.oreoluwa.RedditClone.Services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/comments/")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        commentService.createComment(commentDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping(params = "postId")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@RequestParam("postId") Long postId) {
        return status(OK)
                .body(commentService.getCommentsByPost(postId));
    }

    @GetMapping(params = "username")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUser(@RequestParam("username") String username) {
        return status(OK).body(commentService.getCommentsByUser(username));
    }
}
