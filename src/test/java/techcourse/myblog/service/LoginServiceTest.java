package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.service.dto.domain.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_PASSWORD_2 = "!Q@W3e4r5t";
    private static final String TEST_USERNAME = "test1";

    private static final UserDTO userDTO = new UserDTO(TEST_USERNAME, TEST_EMAIL_1, TEST_PASSWORD_1);

    private User user;
    private UserService userService;
    private LoginService loginService;

    @Autowired
    public LoginServiceTest(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @BeforeEach
    void setUp() {
        user = userService.save(userDTO);
    }

    @Test
    void 로그인_성공_테스트() {
        User user = loginService.getLoginUser(userDTO);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    void 로그인_아이디_찾기_실패_테스트() {
        UserDTO userDTO = new UserDTO(TEST_USERNAME, TEST_EMAIL_2, TEST_PASSWORD_1);

        assertThrows(NotFoundUserException.class, () -> {
            loginService.getLoginUser(userDTO);
        });
    }

    @Test
    void 로그인_패스워드_불일치_테스트() {
        UserDTO userDTO = new UserDTO(TEST_USERNAME, TEST_EMAIL_1, TEST_PASSWORD_2);

        assertThrows(LoginFailException.class, () -> {
            loginService.getLoginUser(userDTO);
        });
    }

    @AfterEach
    void tearDown() {
        userService.delete(user);
    }
}
