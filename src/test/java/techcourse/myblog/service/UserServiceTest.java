package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.exception.SignUpException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    public static final String VALID_PASSWORD = "passWORD1!";

    @Autowired
    UserService userService;

    @Test
    @DisplayName("이메일이 중복되는 경우에 예외를 던져준다.")
    void checkEmailDuplication() {
        UserDto userDto1 = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        UserDto userDto2 = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);

        userService.save(userDto1);

        assertThatThrownBy(() -> userService.save(userDto2))
                .isInstanceOf(SignUpException.class);
    }
}