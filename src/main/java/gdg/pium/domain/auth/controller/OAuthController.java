package gdg.pium.domain.auth.controller;

import gdg.pium.domain.auth.dto.request.KakaoLoginRequest;
import gdg.pium.domain.auth.dto.response.KakaoLoginResponse;
import gdg.pium.domain.auth.dto.response.KakaoLoginUrlResponse;
import gdg.pium.domain.auth.service.OAuthService;
import gdg.pium.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @Operation(
        summary = "[공통] 카카오 로그인 URL 반환",
        description = "카카오 로그인 화면 URL을 반환합니다. 해당 URL로 이동해주세요.",
        responses = {
            @ApiResponse(responseCode = "200", description = "이동 후 사용자가 카카오 로그인을 마칠 시 카카오 인증 서버는 /kakao/callback으로 redirect 시킵니다."),
        }
    )
    @GetMapping("/kakao/auth-uri")
    public ResponseDto<KakaoLoginUrlResponse> kakaoLogin() {
        return ResponseDto.ok(
            KakaoLoginUrlResponse.builder()
            .authorizationUrl(oAuthService.getKakaoAuthorizationUrl())
            .build()
        );
    }

    @Operation(
        summary = "[공통] 카카오 로그인 후 사용자 등록 및 jwt 발급",
        description = "accessToken 을 받아 카카오 서버로 부터 사용자 정보를 받고 그 정보를 DB에 등록한 뒤 jwt 발급",
        responses = {
            @ApiResponse(responseCode = "200")
        }
    )
    @PostMapping("/kakao/login")
    public ResponseDto<KakaoLoginResponse> kakaoCallback(
            @RequestBody KakaoLoginRequest request
    ) {
        return ResponseDto.ok(oAuthService.processKakaoLogin(request));
    }
}
