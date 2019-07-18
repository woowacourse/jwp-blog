package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    @DisplayName("이메일이 중복되는 경우에 예외를 던져준다.")
    void checkEmailDuplication() {
        UserDto userDto1 = new UserDto("name", "email@woowa.com", "password1");
        UserDto userDto2 = new UserDto("name", "email@woowa.com", "password2");

        userService.save(userDto1);

        assertThatThrownBy(() -> userService.save(userDto2))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 2자 미만인 경우에 예외를 던져준다.")
    void underValidNameLength() {
        UserDto userDto = new UserDto("a", "email1@woowa.com", "password");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 10자 초과인 경우에 예외를 던져준다.")
    void exceedValidNameLength() {
        UserDto userDto = new UserDto("abcdefghijk", "email2@woowa.com", "password");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 숫자를 포함하는 경우에 예외를 던져준다.")
    void includeNumberInName() {
        UserDto userDto = new UserDto("abcde1", "email3@woowa.com", "password");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 특수문자를 포함하는 경우에 예외를 던져준다.")
    void includeSpecialCharacterInName() {
        UserDto userDto = new UserDto("abcde!@", "email4@woowa.com", "password");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }
}