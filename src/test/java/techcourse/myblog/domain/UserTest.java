package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void name_constraint() {
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("김철수", "test1@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("a", "test1@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("abcdefghijkl", "test1@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("john123", "test1@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("john!", "test1@example.com", "p@ssW0rd", this::checkEmailDuplicate));
    }

    private boolean checkEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Test
    void password_validation() {
        assertDoesNotThrow(() -> User.of("haaaaa", "test2@example.com", "p@ssW0rd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("haaaaa", "test2@example.com", "p@ssW0", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("haaaaa", "test2@example.com", "passw0rddddd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("haaaaa", "test2@example.com", "p@ssworddddd", this::checkEmailDuplicate));
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("haaaaa", "test2@example.com", "P@SSW0RD", this::checkEmailDuplicate));
    }

    @Test
    void authentication() {
        User newUser = User.of("james", "authentication_test@email.com", "p@ssW0rd");
        assertThat(newUser.authentication("authentication_test@email.com", "p@ssW0rd")).isTrue();
        assertThat(newUser.authentication("authentication_test@email.com", "p@ssW0rd23")).isFalse();
        assertThat(newUser.authentication("authentication_test123@email.com", "p@ssW0rd")).isFalse();
        assertThat(newUser.authentication("auth_test@email.com", "p@ssW0rd")).isFalse();
    }
}
