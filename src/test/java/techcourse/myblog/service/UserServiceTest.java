package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.UserDto;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserServiceTest {
    private static final String NAME = "yusi";
    private static final String EMAIL = "temp@mail.com";
    private static final String PASSWORD = "12345abc";

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        userService.deleteAll();
    }

    @Test
    public void save() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        User actual = userService.save(signUpUserInfo);

        User expected = signUpUserInfo.toUser();
        expected.setId(actual.getId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Id로_User를_조회")
    public void find() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        User expected = userService.save(signUpUserInfo);

        assertEquals(expected, userService.findById(expected.getId()));
    }

    @Test
    public void findByLoginInfo() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        User expected = userService.save(signUpUserInfo);
        UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);

        assertEquals(expected, userService.findByLoginInfo(loginInfo));
    }

    @Test
    public void exitsByEmail() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        userService.save(signUpUserInfo);

        assertTrue(userService.exitsByEmail(signUpUserInfo));
    }

    @Test
    @DisplayName("로그인_가능")
    public void canLogin() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        userService.save(signUpUserInfo);

        UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);

        assertTrue(userService.canLogin(loginInfo));
    }

    @Test
    public void 로그인_불가능_테스트() {
        UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);

        assertFalse(userService.canLogin(loginInfo));
    }

    @Test
    public void deleteByEmail() {
        UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
        userService.save(signUpUserInfo);

        userService.deleteByEmail(EMAIL);
    }

}