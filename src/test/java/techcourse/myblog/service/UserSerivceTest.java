package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSerivceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_PASSWORD_2 = "!Q@W3e4r5t";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_USERNAME_2 = "test2";
    private static final UserDTO userDTO = new UserDTO(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userService.save(userDTO);
    }

    @Test
    void 유저_저장_테스트() {
        assertThat(userService.getUsers().size()).isEqualTo(1);
    }

    @Test
    void 이메일로_중복인지_테스트() {
        UserDTO userDTOTest = new UserDTO(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
        assertThat(userService.isDuplicateEmail(userDTOTest)).isTrue();
    }

    @Test
    void 이메일로_중복아닌지_테스트() {
        UserDTO userDTOTest = new UserDTO(TEST_USERNAME_1, TEST_EMAIL_2, TEST_PASSWORD_1);
        assertThat(userService.isDuplicateEmail(userDTOTest)).isFalse();
    }

    @Test
    void 로그인_성공_테스트() {
        LoginDTO loginDTO = new LoginDTO(TEST_EMAIL_1, TEST_PASSWORD_1);
        User user = userService.getLoginUser(loginDTO);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    void 로그인_아이디_찾기_실패_테스트() {
        LoginDTO loginDTO = new LoginDTO(TEST_EMAIL_2, TEST_PASSWORD_1);
        assertThrows(UserNotExistException.class, () -> userService.getLoginUser(loginDTO));
    }

    @Test
    void 로그인_패스워드_불일치_테스트() {
        LoginDTO loginDTO = new LoginDTO(TEST_EMAIL_1, TEST_PASSWORD_2);
        assertThrows(LoginFailException.class, () -> userService.getLoginUser(loginDTO));
    }

    @Test
    void 유저_정보_수정_테스트() {
        UserDTO userDTOTest = new UserDTO(TEST_USERNAME_2, TEST_EMAIL_1, TEST_PASSWORD_2);
        assertThat(userService.update(userDTOTest).getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(userService.update(userDTOTest).getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @Test
    void 유저_삭제_테스트() {
        userService.save(new UserDTO(TEST_USERNAME_2, TEST_EMAIL_2, TEST_PASSWORD_2));
        userService.delete(TEST_EMAIL_2);
        assertThat(userService.getUsers().size()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        userService.delete(TEST_EMAIL_1);
    }
}
