package gdg.pium.domain.post.dto.response;

import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostInfoResponse {

    private Long postId;
    private String title;
    private String content;
    private String nickname;
    private float latitude;
    private float longitude;
    private int like;
    private List<String> images;

    public static PostInfoResponse from(Post post, User user, List<String> images) {
        return PostInfoResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(user.getNickname())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .like(post.getLikes())
                .images(images)
                .build();
    }
}
