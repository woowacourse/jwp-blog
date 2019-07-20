package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserEmail;
import techcourse.myblog.domain.UserException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("andole", "A!1bcdef", "andole@gmail.com"));
    }

    @Test
    void register_bad_case() {
        assertThatThrownBy(() -> new User("z", "b", "c")).isInstanceOf(UserException.class);
    }

    @Test
    void findByNameTest() {
        assertThat(userRepository.findByEmail(UserEmail.of("andole@gmail.com"))).isNotNull();
        assertThatThrownBy(() -> userRepository.findByEmail(UserEmail.of("abc@gmail.com")).orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

}