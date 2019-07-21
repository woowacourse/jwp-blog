package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.repository.UserRepository;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void duplicate_email() {
        User preExists = User.of("john", "abcde@example.com", "p@ssW0rd", email -> false);
        assertThrows(User.UserCreationConstraintException.class,
            () -> User.of("james", "abcde@example.com", "p@ssW0rd", preExists.getEmail()::equals));
    }

    @Test
    void authentication() {
        User newUser = User.of("james", "authentication_test@email.com", "p@ssW0rd", email -> false);
        assertThat(newUser.authenticate("p@ssW0rd")).isTrue();
        assertThat(newUser.authenticate("p@ssW0rd23")).isFalse();
    }
}
