package techcourse.myblog.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private static final User USER_TO_INSERT = User.of("james", "repository_test@example.com", "p@ssW0rd");

    @Test
    void create() {
        assertThat(userRepository.save(USER_TO_INSERT).getId()).isNotNull();
    }

    @Test
    void findByEmail() {
        User user = userRepository.save(USER_TO_INSERT);
        Optional<User> maybeFound = userRepository.findByEmail(user.getEmail());
        assertThat(maybeFound.isPresent()).isTrue();
        assertThat(maybeFound.get()).isEqualTo(user);
    }
}
