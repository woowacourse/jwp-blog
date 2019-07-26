package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_PASSWORD_2 = "%T^Y&U*I";
    private static final String TEST_USERNAME_2 = "test2";

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
        userRepository.save(user);
    }

    @Test
    void 유저_저장() {
        assertTrue(userRepository.findByEmail(TEST_EMAIL_1).isPresent());
    }

    @Test
    void 유저_존재() {
        assertThat(userRepository.existsByEmail(TEST_EMAIL_1)).isEqualTo(true);
    }

    @Test
    void 유저_이메일_정보로_유저_찾기() {
        assertThat(userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다."))
                .getUserName()).isEqualTo(TEST_USERNAME_1);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다."))
                .getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다."))
                .getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    void 유저_정보_수정() {
        user.setUserName(TEST_USERNAME_2);
        user.setPassword(TEST_PASSWORD_2);
        userRepository.save(user);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다."))
                    .getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다."))
                .getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @Test
    void 유저_삭제() {
        userRepository.delete(user);
        assertThrows(UserNotExistException.class , () -> userRepository.findByEmail(TEST_EMAIL_1)
                .orElseThrow(() -> new UserNotExistException("유저정보가 없습니다.")));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
