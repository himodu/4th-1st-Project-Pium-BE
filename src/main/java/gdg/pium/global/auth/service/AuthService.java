package gdg.pium.global.auth.service;

import com.example.mymoo.global.auth.dto.request.AccountLoginRequestDto;
import com.example.mymoo.global.auth.dto.response.AccountLoginResponseDto;
import com.example.mymoo.global.auth.dto.response.TokenRefreshResponseDto;
import gdg.pium.global.auth.dto.request.AccountLoginRequestDto;
import gdg.pium.global.auth.dto.response.AccountLoginResponseDto;
import gdg.pium.global.auth.dto.response.TokenRefreshResponseDto;

public interface AuthService {
    AccountLoginResponseDto login(AccountLoginRequestDto loginRequestDto);
    TokenRefreshResponseDto getNewAccessToken(String refreshToken);

    void logout(Long AccountId);
}
