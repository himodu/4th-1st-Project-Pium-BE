package gdg.pium.domain.like.controller;

import gdg.pium.domain.like.service.LikeService;
import gdg.pium.global.annotation.UserId;
import gdg.pium.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    @Operation(
            summary = "좋아요 생성",
            description = "좋아요 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "좋아요 생성 성공"),
            }
    ) public ResponseDto<Void> createPostLike(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable(name = "postId") Long postId
    ) {
        likeService.createPostLike(userId, postId);
        return ResponseDto.created(null);
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "좋아요 삭제",
            description = "좋아요 삭제 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "좋아요 삭제 성공"),
            }
    ) public ResponseDto<Void> deletePostLike(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable(name = "postId") Long postId
    ) {
        likeService.deletePostLike(userId, postId);
        return ResponseDto.noContent();
    }
}
