package gdg.pium.domain.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateRequest {

    private String title;
    private String content;
    private List<String> deleteImages;
}
