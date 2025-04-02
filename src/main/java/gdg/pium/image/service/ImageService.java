package gdg.pium.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
            fileUrls.add(fileUrl);
        }

        return fileUrls;
    }

    private String generateUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    public void deleteFileByUrl(String url) {
        String fileName = extractFileNameFromUrl(url);
        amazonS3.deleteObject(bucketName, fileName);
    }

    public String extractFileNameFromUrl(String url) {
        try {
            // URL에서 host를 제외한 path 부분만 추출
            URL s3Url = new URL(url);
            return s3Url.getPath().substring(1); // 앞의 '/' 제거
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 URL 형식입니다: " + url);
        }
    }
}
