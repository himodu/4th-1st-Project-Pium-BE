package gdg.pium.global.auth.service.impl;

import gdg.pium.domain.user.entity.User;
import gdg.pium.domain.user.repository.UserRepository;
import gdg.pium.global.auth.repository.AuthRepository;
import gdg.pium.global.enums.UserRole;
import gdg.pium.global.auth.dto.kakao.KakaoTokenResponse;
import gdg.pium.global.auth.dto.kakao.KakaoUserInfo;
import gdg.pium.global.auth.dto.response.KakaoLoginResponseDto;
import gdg.pium.global.auth.service.OAuthService;
import gdg.pium.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    @Value("${kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    @Override
    public String getKakaoAuthorizationUrl() {
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code-additional-consent
        return UriComponentsBuilder.fromUriString(kakaoAuthorizationUri)
            .queryParam("response_type", "code")
            .queryParam("client_id", kakaoClientId)
            .queryParam("redirect_uri", kakaoRedirectUri) // TODO - 배포 시 변경
            // https://developers.kakao.com/docs/latest/ko/kakaologin/utilize#scope-user
            .queryParam("scope", "profile_nickname,profile_image,account_email")
            .toUriString();
    }

    @Override
    public KakaoLoginResponseDto processKakaoCallback(String code) {
        // 카카오 액세스 토큰 요청
        String kakaoAccessToken = getAccessToken(code);
        log.info("Received kakao access token: {}", kakaoAccessToken);

        // 사용자 정보 요청
        KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);
        log.info("Received kakao user info: {}", userInfo);

        // 신규 유저인지 확인
        String kakaoUserEmail = userInfo.id() + "_" + userInfo.kakaoAccount().email();
        Optional<User> accountOpt = userRepository.findByEmail(kakaoUserEmail);

        if (accountOpt.isEmpty()) {
            // 신규 유저라면 계정 생성
            User newAccount = createNewAccount(userInfo);
            userRepository.save(newAccount);

            return handleLogin(newAccount, true);
        } else {
            User existingAccount = accountOpt.get();
            return handleLogin(existingAccount, false);
        }
    }

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
    private String getAccessToken(String code) {
        log.info("processing code: {}", code);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoClientId);
        formData.add("redirect_uri", kakaoRedirectUri);
        formData.add("code", code);
        return webClient.post()
            .uri(kakaoTokenUri)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(KakaoTokenResponse.class)
            .transform(mono -> applyKakaoApiErrorHandling(mono, "access token"))
            .map(KakaoTokenResponse::accessToken)
            .block(); // 동기 요청
    }

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    private KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.post()
            .uri(kakaoUserInfoUri)
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#propertykeys
            .body(BodyInserters.fromFormData("property_keys", "[\"kakao_account.email\",\"kakao_account.profile\"]"))
            .retrieve()
            .bodyToMono(KakaoUserInfo.class)
            .transform(mono -> applyKakaoApiErrorHandling(mono, "user info"))
            .block(); // 동기 요청
    }

    private User createNewAccount(KakaoUserInfo userInfo) {
        KakaoUserInfo.KakaoAccount kakaoAccount = Objects.requireNonNull(
            userInfo.kakaoAccount(),
            "KakaoAccount cannot be null"
        );
        KakaoUserInfo.KakaoAccount.Profile profile = Objects.requireNonNull(
            kakaoAccount.profile(),
            "Profile cannot be null"
        );
        String nickname = Objects.requireNonNull(
            profile.nickname(),
            "Nickname cannot be null as it is a mandatory consent item in Kakao Login"
        );
        String kakaoUserEmail = userInfo.id() + "_" + userInfo.kakaoAccount().email();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        String profileImageUrl = profile.profileImageUrl();
        log.info("received kakao data. email: {} nickname: {} profileImageUrl: {}", kakaoUserEmail, nickname, profileImageUrl);
        // Profile image URL can be null as it's an optional consent item in Kakao Login
        if (profileImageUrl == null){
            profileImageUrl = getDefaultImage();
        }
        return User.builder()
            .email(kakaoUserEmail)
            .password(password)
            .nickname(nickname)
            .profileImageUrl(profileImageUrl)
            .role(UserRole.USER) // default 계정 : 후원자
            .build();
    }

    private KakaoLoginResponseDto handleLogin(User account, boolean isNewUser) {
        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessTokenWithAccountEntity(account);
        String refreshToken = jwtTokenProvider.createRefreshTokenWithAccountEntity(account);

        // refresh 토큰 저장
        authRepository.saveRefreshToken(
            account.getId(),
            refreshToken,
            JwtTokenProvider.REFRESH_TOKEN_VALIDITY
        );

        // Jwt 및 로그인 정보 전달
        return KakaoLoginResponseDto.builder()
            .accountId(account.getId())
            .userRole(account.getRole().getAuthority())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isNewUser(isNewUser)
            .build();
    }
    private <T> Mono<T> applyKakaoApiErrorHandling(Mono<T> mono, String operationName) {
        return mono.onErrorResume(error -> {
            log.error("Error occurred while retrieving Kakao {}: {}", operationName, error.getMessage());
            return Mono.empty(); // 에러 발생 시 빈 Mono 반환
        });
    }
    private String getDefaultImage(){
        if (Math.random() < 0.5) {
            return "https://";
        } else {
            return "https://";
        }
    }
}
