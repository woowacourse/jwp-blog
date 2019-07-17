package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void Email로_중복_여부_참_테스트() {
        User user = new User();
        user.setId(1L);
        user.setName("테스트");
        user.setEmail("test@woowahan.com");
        user.setPassword("12345678");
        userRepository.save(user);
        assertTrue(userRepository.existsByEmail("test@woowahan.com"));
    }

    @Test
    public void Email로_중복_여부_거짓_테스트() {
        User user = new User();
        user.setId(1L);
        user.setName("테스트");
        user.setEmail("test@woowahan.com");
        user.setPassword("12345678");
        userRepository.save(user);
        assertFalse(userRepository.existsByEmail("test1@woowahan.com"));
    }
}