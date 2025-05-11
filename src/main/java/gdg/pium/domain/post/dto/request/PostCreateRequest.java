package gdg.pium.domain.post.dto.request;

import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.user.entity.User;
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
                .build();
    }
}
