package techcourse.myblog.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    private static final String NAME = "테스트";
    private static final String EMAIL = "test@woowahan.com";
    private static final String PASSWORD = "123qweASD!";
    private static final User SAMPLE_USER;

    static {
        SAMPLE_USER = new User(NAME, EMAIL, PASSWORD);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void Email로_중복_여부_참_테스트() {
        userRepository.save(SAMPLE_USER);
        assertTrue(userRepository.existsByEmail(EMAIL));
    }

    @Test
    public void Email로_중복_여부_거짓_테스트() {
        userRepository.save(SAMPLE_USER);
        assertFalse(userRepository.existsByEmail("test1@woowahan.com"));
    }

    @Test
    @DisplayName("Email을_기준으로_조회")
    public void findByEmail() {
        User expected = SAMPLE_USER;
        userRepository.save(expected);

        User actual = userRepository.findByEmail(expected.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        assertEquals(expected, actual);
    }
}