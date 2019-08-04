package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    void 중복되지_않은_User_생성() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        assertDoesNotThrow(() -> userService.save(userDto));

        userService.removeByEmail(email);
    }

    @Test
    void 중복된_User_생성_예외발생() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        assertDoesNotThrow(() -> userService.save(userDto));
        assertThrows(DuplicatedIdException.class, () -> userService.save(userDto));

        System.out.println("gg");
        userService.removeByEmail(email);
    }

    @Test
    void 저장된_User_조회() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        userService.save(userDto);
        UserDto foundUser = userService.findByEmail(email);

        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getName()).isEqualTo(name);
        assertThat(foundUser.getPassword()).isEqualTo(password);
        userService.removeByEmail(email);
    }

    @Test
    void 저장되지_않은_User_조회_예외발생() {
        String email = "zino1@naver.com";

        assertThrows(NotExistUserIdException.class, () -> userService.findByEmail(email));
    }

    @Test
    void 저장된_User_삭제() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        userService.save(userDto);

        assertDoesNotThrow(() -> userService.removeByEmail(email));
    }

    @Test
    void 저장된_id_login() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        userService.save(userDto);

        assertDoesNotThrow(() -> userService.login(LoginDto.of(userDto)));

        userService.removeByEmail(email);
    }

    @Test
    void 저장되지_않은_id_login() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);

        assertThrows(NotExistUserIdException.class, () -> userService.login(LoginDto.of(userDto)));
    }

    @Test
    void 비밀번호_불일치_login_예외발생() {
        String email = "zino@naver.com";
        String name = "zino";
        String password = "zinozino";

        UserDto userDto = new UserDto(email, name, password);
        userService.save(userDto);

        //userDto.setPassword("zinozinozi");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(LoginDto.of(userDto)));

        userService.removeByEmail(email);
    }
}
