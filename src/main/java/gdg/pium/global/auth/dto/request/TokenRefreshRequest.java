package gdg.pium.global.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
    @NotBlank(message = "refreshToken은 필수 항목입니다")
    String refreshToken
) {

}
