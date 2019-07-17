package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void register_bad_case() {
        User user = new User("z", "b", "c");
    }

    @Test
    void findByNameTest() {
        assertThat(userRepository.findByEmail("andole")).isNotNull();
        assertThat(userRepository.findByEmail("andole")).isNotEqualTo(userRepository.findByEmail("aaa"));
    }
}