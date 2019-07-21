package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
    }

    @Test
    void 정상_로그인() {
        assertDoesNotThrow(() -> loginService.loginByEmailAndPwd(new UserDto("andole", "A!1bcdefg", "andole@gmail.com")));
    }

    @Test
    void 이메일_없음() {
        assertThatThrownBy(() -> loginService.loginByEmailAndPwd(new UserDto("andole", "A!1bcdefg", "abc@abc.com")))
                .isInstanceOf(UserException.class);
    }

    @Test
    void 비밀번호_다름() {
        assertThatThrownBy(() -> loginService.loginByEmailAndPwd(new UserDto("andole", "B!1bcdefg", "andole@gmail.com")))
                .isInstanceOf(UserException.class);
    }
}