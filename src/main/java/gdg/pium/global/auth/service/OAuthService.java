package gdg.pium.global.auth.service;

import gdg.pium.global.auth.dto.response.KakaoLoginResponseDto;

public interface OAuthService {

    String getKakaoAuthorizationUrl();

     KakaoLoginResponseDto processKakaoCallback(String code);
}
