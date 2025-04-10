package gdg.pium.domain.image.controller;

import gdg.pium.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    @Operation(
            summary = "이미지 업로드",
            description = "이미지 업로드용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이미지 업로드 성공"),
            }
    ) public ResponseEntity<?> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> uploadedUrls = imageService.uploadFiles(files);
            return ResponseEntity.ok(uploadedUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 실패: " + e.getMessage());
        }
    }
}
