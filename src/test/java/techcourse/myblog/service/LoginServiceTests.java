package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.presentation.MainControllerTests;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTests {

    private static final Logger log = LoggerFactory.getLogger(MainControllerTests.class);

    private final String NAME = "name";
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "Email123!";
    private final LoginService loginService;
    private final UserService userService;
    private User savedUser;

    @Autowired
    public LoginServiceTests(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        savedUser = userService.save(new User(NAME, EMAIL, PASSWORD));
    }

    @Test
    void 존재하지_않는_이메일_확인_테스트() {
        boolean actual = loginService.notExistUserEmail("notExist@email.com");
        assertThat(actual).isTrue();
    }

    @Test
    void 존재하는_이메일_확인_성공_테스트() {
        boolean actual = loginService.notExistUserEmail(EMAIL);
        assertThat(actual).isFalse();
    }

    @Test
    void 이메일_패스워드_확인_성공_테스트() {
        boolean actual = loginService.matchEmailAndPassword(EMAIL, PASSWORD);
        assertThat(actual).isTrue();
    }

    @Test
    void 이메일_확인_실패_테스트() {
        boolean actual = loginService.matchEmailAndPassword("WrongEmail", PASSWORD);
        assertThat(actual).isFalse();
    }

    @Test
    void 패스워드_확인_실패_테스트() {
        boolean actual = loginService.matchEmailAndPassword(EMAIL, "WrongPassword");
        assertThat(actual).isFalse();
    }

    @Test
    void Email로_USER_찾기_성공_테스트() {
        User foundUser = loginService.findUserByEmail(EMAIL);
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(savedUser.getId());
    }
}
