package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static techcourse.myblog.service.UserServiceTest.USER_NAME;

@ExtendWith(SpringExtension.class)
class LoginServiceTest {
    private static final String TEST_EMAIL = "test1@test.com";
    private static final String ABSENT_EMAIL = "test2@test.com";
    private static final String TEST_PASSWORD = "!Q@W3e4r";
    private static final String WRONG_PASSWORD = "!Q@W3e4r5t";


    @InjectMocks
    LoginService loginService;

    @Mock
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    @DisplayName("로그인에 성공한다")
    void login() {
        LoginDto correctLoginDto = new LoginDto(TEST_EMAIL, TEST_PASSWORD);

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        assertThat(loginService.getLoginUser(correctLoginDto).getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("아이디가 없는 경우 로그인에 실패한다.")
    void failLoginWhenUserHasNoId() {
        LoginDto wrongIdLoginDto = new LoginDto(ABSENT_EMAIL, TEST_PASSWORD);

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            loginService.getLoginUser(wrongIdLoginDto);
            verify(userRepository, atLeastOnce()).findByEmail(wrongIdLoginDto.getEmail());
        });
    }

    @Test
    @DisplayName("패스워드가 일치하지 않는 경우 로그인에 실패한다.")
    void failLoginWhenWrongPassword() {
        LoginDto wrongPasswordLoginDto = new LoginDto(TEST_EMAIL, WRONG_PASSWORD);

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));

        assertThrows(LoginFailException.class, () -> {
            loginService.getLoginUser(wrongPasswordLoginDto);
            verify(userRepository, atLeastOnce()).findByEmail(wrongPasswordLoginDto.getEmail());
        });
    }
}