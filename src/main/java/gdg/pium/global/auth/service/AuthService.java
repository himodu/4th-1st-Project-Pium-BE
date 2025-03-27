package gdg.pium.global.auth.service;


import gdg.pium.global.auth.dto.request.AccountLoginRequestDto;
import gdg.pium.global.auth.dto.response.AccountLoginResponseDto;
import gdg.pium.global.auth.dto.response.TokenRefreshResponseDto;

public interface AuthService {
    AccountLoginResponseDto login(AccountLoginRequestDto loginRequestDto);
    TokenRefreshResponseDto getNewAccessToken(String refreshToken);

    void logout(Long AccountId);
}
