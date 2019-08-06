package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class LoginServiceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_PASSWORD_2 = "!Q@W3e4r5t";
    private static final String TEST_USERNAME_1 = "test1";

    private static final User USER_1 = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final LoginDto LOGIN = new LoginDto(TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final LoginDto WRONG_LOGIN_ID = new LoginDto(TEST_EMAIL_2, TEST_PASSWORD_1);
    private static final LoginDto WRONG_PASSWORD_ID = new LoginDto(TEST_EMAIL_1, TEST_PASSWORD_2);

    @InjectMocks
    LoginService loginService;

    @Mock
    UserRepository userRepository;

    @Test
    void 로그인_성공_테스트() {
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));
        assertThat(loginService.getLoginUser(LOGIN).getEmail()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    void 로그인_아이디_찾기_실패_테스트() {
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            loginService.getLoginUser(WRONG_LOGIN_ID);
            verify(userRepository, atLeastOnce()).findByEmail(WRONG_LOGIN_ID.getEmail());
        });
    }

    @Test
    void 로그인_패스워드_불일치_테스트() {
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        assertThrows(LoginFailException.class, () -> {
            loginService.getLoginUser(WRONG_PASSWORD_ID);
            verify(userRepository, atLeastOnce()).findByEmail(WRONG_PASSWORD_ID.getEmail());
        });
    }
}