package gdg.pium.domain.placedepartment.repository;

import gdg.pium.domain.placedepartment.entity.PlaceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceDepartmentRepository extends JpaRepository<PlaceDepartment, Long> {

    @Query("SELECT d.name FROM PlaceDepartment pd " +
            "JOIN pd.department d " +
            "WHERE pd.place.id = :placeId")
    List<String> findDepartmentNamesByPlaceId(@Param("placeId") Long placeId);
}
