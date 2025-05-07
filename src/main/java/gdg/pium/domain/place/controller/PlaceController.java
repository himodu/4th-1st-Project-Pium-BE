package gdg.pium.domain.place.controller;

import gdg.pium.domain.place.dto.response.PlaceSearchResponse;
import gdg.pium.domain.place.service.PlaceService;
import gdg.pium.global.annotation.UserId;
import gdg.pium.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
