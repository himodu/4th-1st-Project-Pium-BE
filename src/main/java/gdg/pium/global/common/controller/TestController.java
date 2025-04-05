package gdg.pium.global.common.controller;

import gdg.pium.global.annotation.UserId;
import gdg.pium.global.auth.dto.request.AccountLoginRequestDto;
import gdg.pium.global.auth.dto.response.AccountLoginResponseDto;
import gdg.pium.global.common.dto.ResponseDto;
import gdg.pium.global.common.dto.TestResponse;
import gdg.pium.global.common.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @Operation(
            summary = "테스트 용",
            description = "jwt 인증 인가 테스트용 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "테스트 성공 시 성공했다가 보내드릴게요!"),
            }
    )
    @GetMapping("/test")
    public ResponseDto<TestResponse> test(
            @Parameter(hidden = true) @UserId Long userId
    ) {
        return ResponseDto.ok(testService.test(userId));
    }
}
