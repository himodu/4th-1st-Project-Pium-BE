package gdg.pium.domain.post.dto.response;

import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.user.entity.User;
import gdg.pium.global.util.DateTimeUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostInfoResponse {

    private final Long postId;
    private final String userName;
    private final String userProfileImageUrl;
    private final String title;
    private final String content;
    private final String nickname;
    private final float latitude;
    private final float longitude;
    private final int likeCount;
    private final List<String> images;
    private final String createdAt;
    private final Boolean liked;

    public static PostInfoResponse of(Post post, User user, List<String> images, Boolean liked) {
        return PostInfoResponse.builder()
                .postId(post.getId())
                .userName(post.getUser().getNickname())
                .userProfileImageUrl(post.getUser().getProfileImageUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(user.getNickname())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .likeCount(post.getLikes().size())
                .liked(liked)
                .images(images)
                .createdAt(DateTimeUtil.convertLocalDateTimeToString(post.getCreatedAt()))
                .build();
    }

    public static PostInfoResponse of(Post post, User user, List<String> images, Integer likeCount, Boolean liked) {
        return PostInfoResponse.builder()
                .postId(post.getId())
                .userName(user.getNickname())
                .userProfileImageUrl(user.getProfileImageUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(user.getNickname())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .likeCount(likeCount == null ? 0 : likeCount)
                .liked(liked != null)
                .images(images)
                .createdAt(DateTimeUtil.convertLocalDateTimeToString(post.getCreatedAt()))
                .build();
    }
}
