package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserEmail;
import techcourse.myblog.domain.User.UserException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.save(new User("andole", "A!1bcdef", "andole@gmail.com"));
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

}