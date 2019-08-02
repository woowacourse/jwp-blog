package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.domain.user.User;
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
        userService.updateUser(Email.of("andole@gmail.com"), new UserDto("test", "A!1bcdefg", "test@gmail.com"));
        assertThat(userService.findAll().get(0).getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void 회원삭제_테스트() {
        userService.deleteUser(Email.of("andole@gmail.com"));
        assertThat(userService.findAll().size()).isEqualTo(0);
    }
}