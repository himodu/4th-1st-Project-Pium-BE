package gdg.pium.global.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoLoginRequest(
        @JsonProperty("access_token") String accessToken
) {
}
