package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_PASSWORD_2 = "%T^Y&U*I";
    private static final String TEST_USERNAME_2 = "test2";

    UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        User user = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
        userRepository.save(user);
    }

    @Test
    void 유저_이메일_정보로_유저_찾기() {
        assertThat(userRepository.existsByEmail(TEST_EMAIL_1)).isTrue();
    }

    @Test
    void 유저_정보_수정() {
        userRepository.updateUserByEmailAddress(TEST_USERNAME_2, TEST_PASSWORD_2, TEST_EMAIL_1);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1).get().getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1).get().getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @Test
    void 유저_삭제() {
        userRepository.deleteAll();
        assertThat(userRepository.existsByEmail(TEST_EMAIL_1)).isFalse();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
