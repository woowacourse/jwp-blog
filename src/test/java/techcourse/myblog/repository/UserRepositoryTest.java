package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {
    private static final long DEFAULT_USER_ID = 0L;
    private static final String NAME = "유효한이름";
    private static final String EMAIL = "valid@email.com";
    private static final String PASSWORD = "ValidPassword!123";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(DEFAULT_USER_ID, NAME, EMAIL, PASSWORD);
        userRepository.save(user);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 등록된_이메일로_회원이_조회되는지_테스트() {
        assertThat(userRepository.findUserByEmail(EMAIL)).isEqualTo(user);
    }

    @Test
    public void 등록된_이메일에_매칭되는_User객체수가_조회되는지_테스트() {
        assertThat(userRepository.countByEmail(EMAIL)).isEqualTo(1);
    }

    @Test
    public void 등록된_이메일과_비밀번호를_입력했을때_알맞은__User객체가_조회되는지_테스트() {
        assertThat(userRepository.findUserByEmailAndPassword(EMAIL, PASSWORD)).isEqualTo(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}