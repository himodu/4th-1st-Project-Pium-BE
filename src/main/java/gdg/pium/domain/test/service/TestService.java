package gdg.pium.domain.test.service;

import gdg.pium.domain.test.dto.TestResponse;
import gdg.pium.domain.user.entity.User;
import gdg.pium.domain.user.repository.UserRepository;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserRepository userRepository;

    public TestResponse test(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        return new TestResponse(user.getNickname() + "님 접속 성공하셨습니다.");
    }
}
