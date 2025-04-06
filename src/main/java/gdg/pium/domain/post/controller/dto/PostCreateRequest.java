package gdg.pium.domain.post.controller.dto;

import gdg.pium.domain.post.Post;
import gdg.pium.domain.user.User;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    private String title;
    private String content;
    private float latitude;
    private float longitude;

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .latitude(latitude)
                .longitude(longitude)
                .user(user)
                .likes(0)
                .build();
    }
}
