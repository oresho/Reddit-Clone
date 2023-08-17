package com.oreoluwa.RedditClone.Repositories;

import com.oreoluwa.RedditClone.Entities.Post;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.Entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
