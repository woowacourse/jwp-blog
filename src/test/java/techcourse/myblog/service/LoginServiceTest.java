package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.exception.AuthenticationFailException;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {LoginService.class, UserRepository.class})
class LoginServiceTest {
    private static final String TEST_NAME = "hello";
    private static final String TEST_PASSWORD = "aQ!123123";
    private static final String TEST_EMAIL = "hello@world.com";

    @Autowired
    private LoginService loginService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Test
    void authenticate_userIsCorrectToUserRequestDto_returnUser() {
        User user = new User(TEST_NAME, TEST_PASSWORD, TEST_EMAIL);
        UserRequestDto dto = new UserRequestDto(user);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));


        assertThat(loginService.authenticate(dto)).isEqualTo(user);
    }

    @Test
    void authenticate_notSavedUser_throwsAuthenticationFailException() {
        User notExistUser = new User(TEST_NAME, TEST_PASSWORD, TEST_EMAIL);
        UserRequestDto dto = new UserRequestDto(notExistUser);

        given(userRepository.findByEmail(notExistUser.getEmail())).willReturn(Optional.empty());


        assertThrows(AuthenticationFailException.class, () -> loginService.authenticate(dto));
    }

    @Test
    void authenticate_wrongDto_throwsAuthenticationFailException() {
        User user = new User(TEST_NAME, TEST_PASSWORD, TEST_EMAIL);
        UserRequestDto wrongDto = new UserRequestDto(user);
        wrongDto.setPassword("wrong password");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());


        assertThrows(AuthenticationFailException.class, () -> loginService.authenticate(wrongDto));
    }

    @Test
    void login_called_sessionSetAttribute() {
        User user = new User(TEST_NAME, TEST_PASSWORD, TEST_EMAIL);
        UserRequestDto dto = new UserRequestDto(user);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        HttpSession session = mock(HttpSession.class);

        loginService.login(session, new UserRequestDto(user));


        verify(session).setAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY, user);
    }

    @Test
    void logout_called_sessionRemoveAttribute() {
        HttpSession session = mock(HttpSession.class);


        loginService.logout(session);


        verify(session).removeAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY);
    }
}