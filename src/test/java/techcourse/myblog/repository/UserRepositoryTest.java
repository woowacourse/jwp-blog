package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void register_bad_case() {
        assertThatThrownBy(() -> new User("z", "b", "c")).isInstanceOf(UserException.class);
    }

    @Test
    void findByEmailTest() {
        assertThatThrownBy(() -> userRepository.findByEmailEmail("abc@gmail.com").orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }

    @Test
    void findByEmailTest2() {
        assertDoesNotThrow(() -> userRepository.findByEmailEmail("test@test.com"));
    }

    @Test
    void findByEmail_EmailTest() {
        assertDoesNotThrow(() -> userRepository.findByEmailEmail("test@test.com"));
    }
}