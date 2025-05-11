package gdg.pium.domain.place.dto.response;

import gdg.pium.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceSearchRelationResponse {

    private Long id;
    private String name;

    public static PlaceSearchRelationResponse from(Place place) {
        return new PlaceSearchRelationResponse(place.getId(), place.getName());
    }
}
