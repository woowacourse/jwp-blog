package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UserMismatchException;
import techcourse.myblog.exception.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = userService.save(User.builder()
                .name("이름")
                .email("test@test.com")
                .password("password1!")
                .build());
    }

    @Test
    void checkLogin_email이_존재하지_않는_경우() {
        assertThrows(UserNotFoundException.class, () -> loginService.checkLogin("test2@test.com", user.getPassword()));
    }

    @Test
    void checkLogin_password가_다른_경우() {
        assertThrows(UserMismatchException.class, () -> loginService.checkLogin(user.getEmail(), "password2@"));
    }

    @Test
    void findByEmail() {
        assertThat(loginService.findByEmail(user.getEmail())).isEqualTo(user);
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(user.getId());
    }
}