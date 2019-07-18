package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    void User_생성() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email,name,password);
        assertThat(userService.save(userDto)).isEqualTo(email);
    }

    @Test
    void User_조회() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email,name,password);
        userService.save(userDto);
        UserDto foundUser = userService.findAll().get(0);

        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getName()).isEqualTo(name);
        assertThat(foundUser.getPassword()).isEqualTo(password);
    }
}
