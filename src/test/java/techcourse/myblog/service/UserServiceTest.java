package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private User user = new User("andole", "A!1bcdefg", "andole@gmail.com");

    @BeforeEach
    void setUp() {
        userRepository.saveAndFlush(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(user.getId());
    }

    @Test
    void 회원가입_테스트() {
        int before = userService.findAll().size();
        userService.addUser(new UserDto("abc", "A!1bcdefg", "abc@test.com"));
        assertThat(userService.findAll().size()).isNotEqualTo(before);
    }

    @Test
    void 회원정보_수정_테스트() {
        userService.updateUser(user, new UserDto("zxc", "A!1bcdefg", "zxc@gmail.com"));
        assertDoesNotThrow(() -> userRepository.findByEmailEmail("zxc@gmail.com").orElseThrow(IllegalAccessError::new));
    }

    @Test
    void 회원삭제_테스트() {
        userService.addUser(new UserDto("delete", "A!1bcdefg", "delete@gmail.com"));
        userService.deleteUser("delete@gmail.com");

        assertThatThrownBy(() ->
                userRepository.findByEmailEmail("delete@gmail.com").orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }
}