package com.oreoluwa.RedditClone.Repositories;

import com.oreoluwa.RedditClone.Entities.Comment;
import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
