package gdg.pium.post.controller;

import gdg.pium.global.dto.PagingResponse;
import gdg.pium.post.controller.dto.PostCreateRequest;
import gdg.pium.post.controller.dto.PostInfoResponse;
import gdg.pium.post.controller.dto.PostUpdateRequest;
import gdg.pium.post.service.PostService;
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
    public void createPost(
            @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        Long userId = 1L;
        postService.createPost(request, images, userId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostInfoResponse> getPost(@PathVariable(name = "postId") Long postId) {
        Long userId = 1L;
        PostInfoResponse response = postService.getPostById(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<PagingResponse<PostInfoResponse>> getPosts(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Long userId = 1L;
        PagingResponse<PostInfoResponse> response = postService.getPosts(pageable, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestPart("request") PostUpdateRequest request,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages
    ) throws IOException {
        Long userId = 1L; // 현재 하드코딩, 실제 서비스에서는 인증 정보에서 가져와야 함.
        postService.updatePost(postId, request, newImages, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Long userId = 1L;
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
