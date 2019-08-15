package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.support.encryptor.EncryptHelper;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//@SpringBootTest()
@ExtendWith(SpringExtension.class)
class LoginServiceTest {
    // @Autowired
    @InjectMocks
    private LoginService loginService;

//    @MockBean(name = "httpSession")
    @Mock
    private HttpSession httpSession;

//    @MockBean(name = "user")
    @Mock
    private User user;

//    @MockBean(name = "userService")
    @Mock
    private UserService userService;

//    @MockBean(name = "encryptHelper")
    @Mock
    private EncryptHelper encryptHelper;

    @Test
    void login_잘못된_비밀번호_예외발생() {
        String wrongPassword = "wrongPassword";

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword(wrongPassword);
        given(user.isWrongPassword(wrongPassword, encryptHelper)).willReturn(true);
        given(userService.findUserByEmail(any())).willReturn(user);


        assertThrows(LoginException.class, () -> loginService.login(httpSession, userLoginRequest));
    }

    @Test
    void login_올바른_비밀번호_세션에_등록() {
        String validPassword = "validPassword";

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword(validPassword);
        given(user.isWrongPassword(validPassword, encryptHelper)).willReturn(false);
        given(userService.findUserByEmail(any())).willReturn(user);

        loginService.login(httpSession, userLoginRequest);


        verify(httpSession).setAttribute(LoginService.USER_KEY_IN_SESSION, user);
    }

    @Test
    void logout_세션에서_제거() {
        loginService.logout(httpSession);


        verify(httpSession).removeAttribute(LoginService.USER_KEY_IN_SESSION);
    }
}