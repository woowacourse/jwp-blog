package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원_등록_테스트() {
        User user = new User(0L, "코니코니", "cony@naver.com", "@Password1234");
        userRepository.save(user);

        assertThat(userRepository.findAll()).contains(user);
    }

    @Test
    public void 모든_회원_조회_테스트() {
        User user1 = new User(0L, "에헴", "asda@happy.com", "@Password1234");
        User user2 = new User(0L, "코니", "cony@happy.com", "@Password1234");
        List<User> users = Arrays.asList(user1, user2);
        userRepository.saveAll(users);

        assertThat(userRepository.findAll()).isEqualTo(users);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}