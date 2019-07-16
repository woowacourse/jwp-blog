package techcourse.myblog.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void create() {
        User user = User.of("name", "email", "password");
        assertThat(userRepository.save(user).getId()).isNotNull();
    }

    @Test
    void findByEmail() {
        User user = userRepository.save(User.of("john", "email@gmail.com", "p1Assw@rd"));
        Optional<User> maybeFound = userRepository.findByEmail(user.getEmail());
        assertThat(maybeFound.isPresent()).isTrue();
        assertThat(maybeFound.get()).isEqualTo(user);
    }
}
