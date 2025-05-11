package gdg.pium.domain.place.service;

import gdg.pium.domain.place.dto.response.PlaceSearchRelationResponse;
import gdg.pium.domain.place.dto.response.PlaceSearchResponse;
import gdg.pium.domain.place.entity.Place;
import gdg.pium.domain.place.repository.PlaceRepository;
import gdg.pium.domain.placedepartment.repository.PlaceDepartmentRepository;
import gdg.pium.domain.user.entity.User;
import gdg.pium.domain.user.repository.UserRepository;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceDepartmentRepository placeDepartmentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PlaceSearchResponse> searchPlaces(String keyword, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        List<Place> places = placeRepository.findPlacesByKeyword(keyword);

        List<PlaceSearchResponse> response = places.stream()
                .map(place -> {
                    List<String> departments = placeDepartmentRepository.findDepartmentNamesByPlaceId(place.getId());
                    return PlaceSearchResponse.from(place, departments);
                })
                .collect(Collectors.toList());

        return response;
    }

    @Transactional(readOnly = true)
    public List<PlaceSearchRelationResponse> searchPlaceRelations(String keyword) {
        List<Place> places = placeRepository.findPlacesByNameKeyword(keyword);
        return places.stream()
                .map(PlaceSearchRelationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaceSearchResponse getPlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        List<String> departments = placeDepartmentRepository.findDepartmentNamesByPlaceId(placeId);

        return PlaceSearchResponse.from(place, departments);
    }
}
