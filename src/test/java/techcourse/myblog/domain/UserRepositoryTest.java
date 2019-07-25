package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private static final String USER_NAME = "이름";
    private static final String EMAIL = "username@gmail.com";
    private static final String PASSWORD = "password";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void countByEmail() {
        User user = new User(USER_NAME, EMAIL, PASSWORD);
        User persistUser = userRepository.save(user);

        User dbUser = userRepository.findUserByEmail(EMAIL);

        assertThat(dbUser).isEqualTo(persistUser);
    }
}