package gdg.pium.domain.post.controller.dto;

import gdg.pium.domain.post.Post;
import gdg.pium.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostInfoResponse {

    private String title;
    private String content;
    private String nickname;
    private float latitude;
    private float longitude;
    private int like;
    private List<String> images;

    public static PostInfoResponse from(Post post, User user, List<String> images) {
        return PostInfoResponse.builder()
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
