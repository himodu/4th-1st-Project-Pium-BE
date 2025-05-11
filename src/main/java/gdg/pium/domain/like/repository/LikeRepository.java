package gdg.pium.domain.like.repository;

import gdg.pium.domain.like.entity.Like;
import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Boolean existsByUserIdAndPostId(Long userId, Long postId);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);


    @Query(
            "SELECT l.post.id as postId, " +
                    "COUNT(l) as count " +
                    "FROM Like l " +
                    "WHERE l.post in :posts " +
                    "GROUP BY l.post.id"
    )
    List<PostLikeCount> findPostLikeCountInPosts(List<Post> posts);

    interface PostLikeCount {
        Long getPostId();
        Integer getCount();
    }

    @Query(
            "SELECT l.post.id as postId " +
                    "FROM Like l " +
                    "WHERE l.user = :user " +
                    "AND l.post IN :posts " +
                    "GROUP BY l.post.id"
    )
    List<Long> findPostLikeExistsByUserAndPosts(User user, List<Post> posts);

}
