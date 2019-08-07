package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private LoginDto loginDto = new LoginDto("test@test.com", "A!1bcdefg");

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmailEmail("test@test.com").get();
    }

    @Test
    void 정상_로그인() {
        assertDoesNotThrow(() -> loginService.loginByEmailAndPwd(loginDto));
    }

    @Test
    void 이메일_없음() {
        assertThatThrownBy(() -> loginService.loginByEmailAndPwd(new LoginDto("yang@achi.com", "A!1bcdefg")))
                .isInstanceOf(UserException.class);
    }

    @Test
    void 비밀번호_다름() {
        assertThatThrownBy(() -> loginService.loginByEmailAndPwd(new LoginDto("test@test.com", "Z!1bcdefg")))
                .isInstanceOf(UserException.class);
    }
}