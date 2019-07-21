package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.repository.UserRepository;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void duplicate_email() {
        userRepository.save(User.of("john", "abcde@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("james", "abcde@example.com", "p@ssW0rd", this::checkEmailDuplicate));
    }

    private boolean checkEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Test
    void authentication() {
        User newUser = User.of("james", "authentication_test@email.com", "p@ssW0rd", this::checkEmailDuplicate);
        assertThat(newUser.authenticate("p@ssW0rd")).isTrue();
        assertThat(newUser.authenticate("p@ssW0rd23")).isFalse();
    }
}
