package techcourse.myblog.service.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LoginServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    private UserResponse defaultUser;
    private String email;
    private String name;
    private String password;

    @BeforeEach
    void setUp() {
        defaultUser = userService.findById(DEFAULT_USER_ID);
        email = defaultUser.getEmail();
        name = defaultUser.getName();
        password = "p@ssW0rd";
    }

    @Test
    void 로그인_성공_확인() {
        UserResponse retrieveUser = loginService.findByEmailAndPassword(email, password);
        assertThat(retrieveUser).isEqualTo(new UserResponse(retrieveUser.getId(), email, name));
    }

    @Test
    void 로그인_실패_확인_비밀번호가_틀린_경우() {
        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword(email, "wrongPassword"));
    }

    @Test
    void 로그인_실패_확인_이메일이_틀린_경우() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword("wrong@gmail.com", password));
    }

    @Test
    void 로그인_오류확인_이메일이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword(null, password));
    }

    @Test
    void 로그인_오류확인_비밀번호가_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword(email, null));
    }
}
