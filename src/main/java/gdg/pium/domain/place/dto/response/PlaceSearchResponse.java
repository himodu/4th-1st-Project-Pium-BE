package gdg.pium.domain.place.dto.response;

import gdg.pium.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceSearchResponse {

    private String name;
    private String manageNumber;
    private String placeImageUrl;
    private String phoneNumber;
    private String homepageUrl;
    private List<String> departments;

    public static PlaceSearchResponse from(Place place, List<String> departments) {
        return PlaceSearchResponse.builder()
                .name(place.getName())
                .manageNumber(place.getManageNumber())
                .placeImageUrl(place.getPlaceImageUrl())
                .phoneNumber(place.getPhoneNumber())
                .homepageUrl(place.getHomepageUrl())
                .departments(departments)
                .build();
    }
}
