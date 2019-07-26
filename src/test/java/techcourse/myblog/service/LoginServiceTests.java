package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.exception.UserNotFoundException;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LoginService.class})
class LoginServiceTests {
    @Autowired
    private LoginService loginService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;

    @Test
    void login_잘못된유저정보_로그인실패() {
        User user = TestUser.createValid();

        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loginService.login(httpSession, user.getEmail(), user.getPassword()));
    }

    @Test
    void login_올바른유저정보_로그인성공() {
        User user = TestUser.createValid();
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).willReturn(Optional.of(user));

        loginService.login(httpSession, user.getEmail(), user.getPassword());

        verify(httpSession).setAttribute(LoginService.USER_ID, user.getId());
    }

    @Test
    void logout_() {
        loginService.logout(httpSession);

        verify(httpSession).removeAttribute(LoginService.USER_ID);
    }

    @Test
    void isLoggedInUser_세션이없을때() {
        long userId = 1;
        long anyId = 1;
        given(httpSession.getAttribute(LoginService.USER_ID)).willReturn(null);

        assertThat(loginService.isLoggedInUser(httpSession, anyId)).isFalse();
    }

    @Test
    void isLoggedInUser_로그인된유저와_확인하려는유저_같음() {
        long userId = 1;
        long id = 1;
        given(httpSession.getAttribute(LoginService.USER_ID)).willReturn(userId);

        assertThat(loginService.isLoggedInUser(httpSession, id)).isTrue();
    }

    @Test
    void isLoggedInUser_로그인된유저와_확인하려는유저_다름() {
        long userId = 1;
        long differentId = userId + 1;
        given(httpSession.getAttribute(LoginService.USER_ID)).willReturn(userId);

        assertThat(loginService.isLoggedInUser(httpSession, differentId)).isFalse();
    }
}