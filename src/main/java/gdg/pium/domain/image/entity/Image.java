package gdg.pium.domain.image.entity;

import gdg.pium.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Getter
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String imageUrl, Post post) {
        this.imageUrl = imageUrl;
        this.post = post;
    }
}
