package gdg.pium.domain.like.service;

import gdg.pium.domain.like.entity.Like;
import gdg.pium.domain.like.repository.LikeRepository;
import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.post.repository.PostRepository;
import gdg.pium.domain.user.entity.User;
import gdg.pium.domain.user.repository.UserRepository;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createPostLike(Long userId, Long postId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new CommonException(ErrorCode.ALREADY_HAVE_LIKE);
        }

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        likeRepository.save(
                Like.builder()
                        .user(user)
                        .post(post)
                        .build()
        );
    }

    @Transactional
    public void deletePostLike(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_LIKE));

        likeRepository.delete(like);
    }

}
