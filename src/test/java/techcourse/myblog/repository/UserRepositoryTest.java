package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_PASSWORD_2 = "%T^Y&U*I";
    private static final String TEST_USERNAME_2 = "test2";

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
        userRepository.save(user);
    }

    @Test
    void 유저_이메일_정보로_유저_찾기() {
        assertThat(userRepository.findByUserEmailAddress(TEST_EMAIL_1).getUserName()).isEqualTo(TEST_USERNAME_1);
        assertThat(userRepository.findByUserEmailAddress(TEST_EMAIL_1).getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(userRepository.findByUserEmailAddress(TEST_EMAIL_1).getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    void 유저_정보_수정() {
        userRepository.updateUserByEmailAddress(TEST_USERNAME_2, TEST_PASSWORD_2, TEST_EMAIL_1);
        assertThat(userRepository.findByUserEmailAddress(TEST_EMAIL_1).getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(userRepository.findByUserEmailAddress(TEST_EMAIL_1).getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
