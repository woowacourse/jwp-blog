package techcourse.myblog.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void DomainValidation체크() {
        UserDto.Register userDto = UserDto.Register.builder()
                .name("van")
                .email("asdf")
                .password("!234Qwer")
                .build();
        User user = new User();

        BeanUtils.copyProperties(userDto, user);
        assertThrows(ValidSingupException.class,() ->userService.validateUser(user));
    }

    @Test
    void DomainValidataion정상체크() {
        UserDto.Register userDto = UserDto.Register.builder()
                .name("van")
                .email("asdf@naver.com")
                .password("!234Qwer")
                .build();
        User user = new User();

        BeanUtils.copyProperties(userDto, user);
        userService.validateUser(user);
    }
}