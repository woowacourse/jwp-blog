package techcourse.myblog.service.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.user.UserRequestDto;
import techcourse.myblog.dto.user.UserResponseDto;
import techcourse.myblog.exception.EmailNotFoundException;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTest {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    private UserResponseDto persistUser;
    private String email;
    private String name;
    private String password;

    @BeforeEach
    void setUp() {
        email = "done@gmail.com";
        name = "done";
        password = "12345678";
        persistUser = userService.save(new UserRequestDto(email, name, password));
    }

    @AfterEach
    void tearDown() {
        userService.delete(persistUser);
    }

    @Test
    void 로그인_성공_확인() {
        UserResponseDto retrieveUser = loginService.findByEmailAndPassword(email, password);
        assertThat(retrieveUser).isEqualTo(new UserResponseDto(email, name));
    }

    @Test
    void 로그인_실패_확인_비밀번호가_틀린_경우() {
        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword(email, "12345677"));
    }

    @Test
    void 로그인_실패_확인_이메일이_틀린_경우() {
        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> loginService.findByEmailAndPassword("dowon@gmail.com", password));
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
