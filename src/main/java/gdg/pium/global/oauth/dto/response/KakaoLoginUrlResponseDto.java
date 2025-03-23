package gdg.pium.global.oauth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record KakaoLoginUrlResponseDto(
    @Schema(description = "카카오 로그인 URL", example = "https://kauth.kakao.com/oauth/authorize?client_id=MY_CLIENT_ID&redirect_uri=MY_REDIRECT_URI&response_type=code")
    @NonNull String authorizationUrl
) {

}
