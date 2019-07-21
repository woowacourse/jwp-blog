package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user = new User(0L, "코니", "cony@cony.com", "@Password1234");
    }

    @Test
    public void 회원_등록_테스트() {
        userRepository.save(user);
        assertThat(userRepository.findAll()).contains(user);
    }

    @Test
    public void 모든_회원_조회_테스트() {
        User user2 = new User(0L, "코니", "cony@happy.com", "@Password1234");
        List<User> users = Arrays.asList(user, user2);
        userRepository.saveAll(users);

        assertThat(userRepository.findAll()).isEqualTo(users);
    }

    @Test
    public void 회원_삭제_테스트() {
        userRepository.save(user);
        userRepository.deleteById(user.getUserId());

        assertThat(userRepository.findAll()).doesNotContain(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}