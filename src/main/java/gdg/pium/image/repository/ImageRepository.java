package gdg.pium.image.repository;

import gdg.pium.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageUrl FROM Image i WHERE i.post.id = :postId")
    List<String> findByPostId(@Param("postId") Long postId);

    List<Image> findEntityByPostId(Long postId);

    List<Image> findAllByPostId(Long postId);
}
