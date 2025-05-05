package gdg.pium.domain.place.repository;

import gdg.pium.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT DISTINCT p FROM Place p " +
            "LEFT JOIN PlaceDepartment pd ON pd.place = p " +
            "LEFT JOIN Department d ON pd.department = d " +
            "LEFT JOIN Lecture l ON l.place = p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Place> findPlacesByKeyword(@Param("keyword") String keyword);
}
