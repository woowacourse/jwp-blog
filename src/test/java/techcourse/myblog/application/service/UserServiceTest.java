package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.exception.LoginFailException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static techcourse.myblog.application.service.UserQueryResult.EMAIL_ALREADY_TAKEN;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.tryRegister(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void save_findByID_isNotNull() {
        assertThat(userRepository.findById((long) 1)).isNotNull();
    }

    @Test
    void save_checkDuplicatedEmail_exception() {
        assertThat(userService.tryRegister(TEST_NAME, TEST_EMAIL, TEST_PASSWORD))
                .isEqualTo(EMAIL_ALREADY_TAKEN);
    }

    @Test
    void tryLogin_emailNotMatch_error() {
        assertThrows(LoginFailException.class, () -> userService.tryLogin(TEST_EMAIL + "not", TEST_PASSWORD));
    }

    @Test
    void tryLogin_success() {
        assertThat(userService.tryLogin(TEST_EMAIL, TEST_PASSWORD)).isEqualTo(userRepository.findByEmail(TEST_EMAIL).get());
    }

    @Test
    void updateUserName_otherData_true() {
        userService.tryUpdate("로비", TEST_USER);
        assertThat(userService.getUserByEmail(TEST_EMAIL).get().getName()).isEqualTo("로비");
    }

    @Test
    void delete_success() {
        String willDeleteEmail = TEST_EMAIL + "delete";
        userService.tryRegister(TEST_NAME, TEST_EMAIL + "delete", TEST_PASSWORD);
        userService.delete(userRepository.findByEmail(willDeleteEmail).get());
        assertThat(userRepository.findByEmail(willDeleteEmail).isPresent()).isEqualTo(false);
    }

}
