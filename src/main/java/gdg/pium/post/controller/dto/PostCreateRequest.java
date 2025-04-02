package gdg.pium.post.controller.dto;

import gdg.pium.post.Post;
import gdg.pium.user.Users;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    private String title;
    private String content;
    private float latitude;
    private float longitude;

    public Post toEntity(Users user) {
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
