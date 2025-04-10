package gdg.pium.domain.post.service;

import gdg.pium.domain.user.entity.User;
import gdg.pium.global.dto.PagingResponse;
import gdg.pium.domain.image.entity.Image;
import gdg.pium.domain.image.repository.ImageRepository;
import gdg.pium.domain.image.service.ImageService;
import gdg.pium.domain.post.entity.Post;
import gdg.pium.domain.post.dto.request.PostCreateRequest;
import gdg.pium.domain.post.dto.response.PostInfoResponse;
import gdg.pium.domain.post.dto.request.PostUpdateRequest;
import gdg.pium.domain.post.repository.PostRepository;
import gdg.pium.domain.user.repository.UserRepository;

import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Transactional
    public void createPost(PostCreateRequest request, List<MultipartFile> images, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Post post = request.toEntity(user);
        postRepository.save(post);

        List<String> imageUrls = imageService.uploadFiles(images);
        for (String imageUrl : imageUrls) {
            imageRepository.save(new Image(imageUrl, post));
        }
    }

    @Transactional(readOnly = true)
    public PostInfoResponse getPostById(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("asdas"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        List<String> imageUrls = imageRepository.findByPostId(postId);

        return PostInfoResponse.from(post, user, imageUrls);
    }

    @Transactional(readOnly = true)
    public PagingResponse<PostInfoResponse> getPosts(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<Post> postPage = postRepository.findAll(pageable);

        Page<PostInfoResponse> responsePage = postPage.map(post -> {
            // 게시글 작성자
            User writer = userRepository.findById(post.getUser().getId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

            // 이미지 URL 조회
            List<String> imageUrls = imageRepository.findByPostId(post.getId());

            return PostInfoResponse.from(post, writer, imageUrls);
        });


        return PagingResponse.from(responsePage);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> newImages, Long userId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.POST_EDIT_FORBIDDEN);
        }

        post.update(request.getTitle(), request.getContent());

        // 기존 이미지 조회
        List<Image> existingImages = imageRepository.findEntityByPostId(postId);

        // 삭제할 이미지 처리
        if (request.getDeleteImages() != null && !request.getDeleteImages().isEmpty()) {
            List<Image> imagesToDelete = existingImages.stream()
                    .filter(image -> request.getDeleteImages().contains(image.getImageUrl()))
                    .collect(Collectors.toList());

            imageRepository.deleteAll(imagesToDelete);

            for (String imageUrl : request.getDeleteImages()) {
                imageService.deleteFileByUrl(imageUrl); // S3에서 삭제
            }
        }

        // 새로운 이미지 업로드 및 저장
        if (newImages != null && !newImages.isEmpty()) {
            List<String> newImageUrls = imageService.uploadFiles(newImages); // 이미지 업로드

            List<Image> newImageEntities = newImageUrls.stream()
                    .map(url -> new Image(url, post))
                    .collect(Collectors.toList());

            imageRepository.saveAll(newImageEntities);
        }
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        // 작성자 검증
        if (!post.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.POST_EDIT_FORBIDDEN);
        }

        // 게시글에 연결된 이미지 S3, DB 삭제
        List<Image> images = imageRepository.findAllByPostId(postId);
        for (Image image : images) {
            imageService.deleteFileByUrl(image.getImageUrl()); // S3에서 삭제
        }

        imageRepository.deleteAll(images); // DB에서 이미지 삭제
        postRepository.delete(post); // 게시글 삭제
    }
}
