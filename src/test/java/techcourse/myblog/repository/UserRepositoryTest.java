package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.SignUpException;

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
        assertThatThrownBy(() -> new User("z", "b", "c")).isInstanceOf(SignUpException.class);
    }

    @Test
    void findByNameTest() {
        assertThat(userRepository.findByEmail("andole@gmail.com")).isNotNull();
        assertThat(userRepository.findByEmail("andole@gmail.com")).isNotEqualTo(userRepository.findByEmail("aaa"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}