package gdg.pium.domain.post.controller;

import gdg.pium.global.annotation.UserId;
import gdg.pium.global.common.dto.ResponseDto;
import gdg.pium.global.dto.PagingResponse;
import gdg.pium.domain.post.dto.request.PostCreateRequest;
import gdg.pium.domain.post.dto.response.PostInfoResponse;
import gdg.pium.domain.post.dto.request.PostUpdateRequest;
import gdg.pium.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "게시물 생성",
            description = "게시물 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시물 생성 성공"),
            }
    ) public ResponseDto<Void> createPost(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        postService.createPost(request, images, userId);
        return ResponseDto.created(null);
    }

    @GetMapping("/{postId}")
    @Operation(
            summary = "게시물 세부 조회",
            description = "게시물 세부 조회용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 세부 조회 성공"),
            }
    ) public ResponseDto<PostInfoResponse> getPost(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable(name = "postId") Long postId
    ) {
        PostInfoResponse response = postService.getPostById(postId, userId);
        return ResponseDto.ok(response);
    }

    @GetMapping("")
    @Operation(
            summary = "게시물 목록 조회",
            description = "게시물 목록 조회용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 목록 조회 성공"),
            }
    ) public ResponseDto<PagingResponse<PostInfoResponse>> getPosts(
            @Parameter(hidden = true) @UserId Long userId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        PagingResponse<PostInfoResponse> response = postService.getPosts(pageable, userId);
        return ResponseDto.ok(response);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "게시물 수정",
            description = "게시물 수정용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "게시물 수정 성공"),
            }
    ) public ResponseDto<Void> updatePost(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable Long postId,
            @RequestPart("request") PostUpdateRequest request,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages
    ) throws IOException {
        postService.updatePost(postId, request, newImages, userId);
        return ResponseDto.noContent();
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "게시물 삭제",
            description = "게시물 삭제용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "테스트 성공 시 성공했다가 보내드릴게요!"),
            }
    ) public ResponseDto<Void> deletePost(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable Long postId
    ) {
        postService.deletePost(postId, userId);
        return ResponseDto.noContent();
    }
}
