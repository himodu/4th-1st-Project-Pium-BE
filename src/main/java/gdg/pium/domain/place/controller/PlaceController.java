package gdg.pium.domain.place.controller;

import gdg.pium.domain.place.dto.response.PlaceSearchRelationResponse;
import gdg.pium.domain.place.dto.response.PlaceSearchResponse;
import gdg.pium.domain.place.service.PlaceService;
import gdg.pium.global.annotation.UserId;
import gdg.pium.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/place")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/search")
    public ResponseDto<List<PlaceSearchResponse>> search(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestParam String keyword) {
        return ResponseDto.ok(placeService.searchPlaces(keyword, userId));
    }

    @GetMapping("/search/relation")
    public ResponseDto<List<PlaceSearchRelationResponse>> searchPlaceRelations(@RequestParam String keyword) {
        List<PlaceSearchRelationResponse> response = placeService.searchPlaceRelations(keyword);
        return ResponseDto.ok(response);
    }

    @GetMapping("/{placeId}")
    public ResponseDto<PlaceSearchResponse> getPlaceDetail(@PathVariable Long placeId) {
        PlaceSearchResponse response = placeService.getPlace(placeId);
        return ResponseDto.ok(response);
    }
}
