package techcourse.myblog.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private static final String EMAIL = "test1@test.com";
    private static final String PASSWORD = "!Q@W3e4r";
    private static final String USERNAME = "test1";

    private User user = new User(USERNAME, EMAIL, PASSWORD);

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(user);
    }

    @Test
    void 유저_이메일_정보로_유저_찾기() {
        assertThat(userRepository.existsByEmail(EMAIL)).isTrue();
    }

    @Test
    void 유저_삭제() {
        userRepository.deleteAll();
        assertThat(userRepository.existsByEmail(EMAIL)).isFalse();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
