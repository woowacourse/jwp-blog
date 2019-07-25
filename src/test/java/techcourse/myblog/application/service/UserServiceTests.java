package techcourse.myblog.application.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.error.DuplicatedEmailException;
import techcourse.myblog.error.WrongPasswordException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTests {
    UserDto userDto;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().name("김강민")
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();
        userService.save(userDto);

    }

    @Test
    void save_findByID_isNotNull() {
        assertThat(userRepository.findById((long) 1)).isNotNull();
    }

    @Test
    void save_checkDuplicatedEmail_exception() {
        UserDto userDto = UserDto.builder().name("abc")
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();
        assertThrows(DuplicatedEmailException.class, () -> userService.save(userDto));
    }

    @Test
    void checkPassword_isMatch_true() {
        LoginDto loginDto = LoginDto.builder()
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();
        assertThat(userService.checkPassword(loginDto)).isTrue();
    }

    @Test
    void checkPassword_isNotMatch_exception() {
        LoginDto loginDto = LoginDto.builder()
                .email("kangmin789@naver.com")
                .password("asdASD12!@1")
                .build();
        assertThrows(WrongPasswordException.class, () -> userService.checkPassword(loginDto));
    }

    @Test
    void getUserDtoByEmail_isPresent_true() {
        assertThat(userService.getUserByEmail(userDto.getEmail()).isPresent()).isTrue();
    }

    @Test
    void getUserDtoByEmail_isNotPresent_false() {
        assertThat(userService.getUserByEmail("abc@naver.com").isPresent()).isFalse();
    }

    @Test
    void updateUserName_otherData_true() {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .build();
        userService.updateUserName(user, "로비");
        assertThat(userService.getUserByEmail(userDto.getEmail()).get().getName()).isEqualTo("로비");
    }

    @AfterEach
    void tearDown() {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .build();
        userService.deleteUser(user);
    }
}
