package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class LoginServiceTests extends MyblogApplicationTests {
    User user;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, USER_PASSWORD, USER_EMAIL);
    }

    @Test
    @DisplayName("이메일 패스워드 일치할떄 예외 던지지 않음")
    void match_password() {
        UserDto userDto = new UserDto(1L, USER_NAME, USER_PASSWORD, USER_EMAIL);
        given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> {
            loginService.checkValidUser(userDto);
        });
    }

    @Test
    @DisplayName("비밀번호 틀렸을 때 예외 던짐")
    void do_not_match_password() {
        UserDto userDto = new UserDto(1L, USER_NAME, "asdASAS1!@", USER_EMAIL);
        given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.checkValidUser(userDto);
        });
    }
}
