package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserEmail;
import techcourse.myblog.domain.user.UserException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
    void findByNameTest() {
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        assertThatThrownBy(() -> userRepository.findByEmail(UserEmail.of("abc@gmail.com")).orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }

    @Test
    void name() {
        User user = userRepository.findByEmail_Email("test@test.com").get();
    }
}