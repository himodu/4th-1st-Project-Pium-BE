package gdg.pium.global.oauth.service;

import gdg.pium.global.oauth.dto.response.KakaoLoginResponseDto;

public interface OAuthService {
    String getKakaoAuthorizationUrl();
     KakaoLoginResponseDto processKakaoCallback(String code);
}
