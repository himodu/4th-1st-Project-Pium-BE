package gdg.pium.global.auth.service.impl;

import gdg.pium.global.auth.dto.request.AccountLoginRequestDto;
import gdg.pium.global.auth.dto.response.AccountLoginResponseDto;
import gdg.pium.global.auth.dto.response.TokenRefreshResponseDto;
import gdg.pium.global.auth.repository.AuthRepository;
import gdg.pium.global.auth.service.AuthService;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import gdg.pium.global.security.CustomUserDetails;
import gdg.pium.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AccountLoginResponseDto login(AccountLoginRequestDto loginRequestDto) {
        // 인증
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.email(),
                loginRequestDto.password()
            )
        );
        log.info("인증 완료: {}", authentication.getName());

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // Refresh 토큰 저장
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = customUserDetails.getUserId();
        authRepository.saveRefreshToken(
            accountId,
            refreshToken,
            JwtTokenProvider.REFRESH_TOKEN_VALIDITY
        );

        String userRole = customUserDetails.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse(null); // 권한이 없는 경우 null 반환
        return AccountLoginResponseDto.builder()
            .accountId(accountId)
            .userRole(userRole)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TokenRefreshResponseDto getNewAccessToken(String refreshToken) {
        // refresh 토큰 유효성 검사
        Claims claims = jwtTokenProvider.getClaimsFromToken(refreshToken); // throws jwtException
        Long accountId = claims.get(JwtTokenProvider.USER_ID_KEY, Long.class);
        String authority = claims.get(JwtTokenProvider.AUTHORIZATION_KEY, String.class);

        // 저장된 Refresh 토큰과 비교
        String savedRefreshToken = authRepository.findRefreshTokenByAccountId(accountId)
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_END_POINT));
        if (!savedRefreshToken.equals(refreshToken)) {
            throw new CommonException(ErrorCode.NOT_FOUND_END_POINT);
        }

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessTokenWithRefreshTokenInfo(
            accountId,
            authority
        );

        return TokenRefreshResponseDto.builder()
            .accessToken(newAccessToken)
            .build();
    }

    @Override
    public void logout(Long accountId) {
        // Refresh Token 삭제
        authRepository.deleteRefreshToken(accountId);
    }
}