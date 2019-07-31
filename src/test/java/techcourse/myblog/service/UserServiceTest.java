package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserEmail;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user = new User("andole", "A!1bcdefg", "andole@gmail.com");

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        userRepository.saveAndFlush(new User("andole", "A!1bcdefg", "andole@gmail.com"));
    }

    @Test
    void 회원가입_테스트() {
        userService.addUser(new UserDto("test", "A!1bcdefg", "test@test.com"));
        assertThat(userService.findAll().size()).isEqualTo(2);
    }

    @Test
    void 회원정보_수정_테스트() {
        userService.updateUser(user, new UserDto("test", "A!1bcdefg", "test@gmail.com"));
        assertDoesNotThrow(() -> userRepository.findByEmail(UserEmail.of("test@gmail.com")).orElseThrow(IllegalAccessError::new));
    }

    @Test
    void 회원삭제_테스트() {
        userService.deleteUser("andole@gmail.com");
        assertThat(userService.findAll().size()).isEqualTo(0);
    }
}