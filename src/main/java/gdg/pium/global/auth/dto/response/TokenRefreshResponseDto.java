package gdg.pium.global.auth.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record TokenRefreshResponseDto(
    @NonNull String accessToken
) {

}
