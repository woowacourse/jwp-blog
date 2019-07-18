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
    void emailDuplicationCheck() {
        UserDto userDto1 = new UserDto("name1", "email@woowa.com", "password1");
        UserDto userDto2 = new UserDto("name2", "email@woowa.com", "password2");

        userService.save(userDto1);

        assertThatThrownBy(() -> userService.save(userDto2))
                .isInstanceOf(SignUpException.class);
    }
}