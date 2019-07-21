package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입_테스트() {
        userService.addUser(new UserDto("test", "A!1bcdefg", "test@test.com"));
        assertThat(userService.findAll().size()).isEqualTo(2);
    }

    @Test
    void 회원정보_수정_테스트() {
        userService.updateUser("andole@gmail.com", new UserDto("test", "A!1bcdefg", "test@gmail.com"));
        assertThat(userService.findAll().get(0).getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void 회원삭제_테스트() {
        userService.deleteUser("andole@gmail.com");
        assertThat(userService.findAll().size()).isEqualTo(0);
    }
}