package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user = new User(0L, "코니", "cony@cony.com", "@Password1234");
    }

    @Test
    public void 회원_가입이_잘_되는지_확인한다() {
        User persistUser = userRepository.save(user);
        assertThat(persistUser).isEqualTo(user);
    }

    @Test
    public void 모든_회원_조회가_잘_되는지_확인한다() {
        User user2 = new User(0L, "코니", "cony@happy.com", "@Password1234");
        List<User> users = Arrays.asList(user, user2);
        userRepository.saveAll(users);

        assertThat(userRepository.findAll()).isEqualTo(users);
    }

    @Test
    public void 회원_탈퇴가_잘_되는지_확인한다() {
        userRepository.save(user);
        userRepository.deleteById(user.getUserId());

        assertThat(userRepository.findAll()).doesNotContain(user);
    }
}