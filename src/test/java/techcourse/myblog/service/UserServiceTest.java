package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.user.UserTest.user;

@SpringBootTest
public class UserServiceTest {

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;

    @Autowired
    private UserService userService;

    private UserDto userDto = new UserDto();

    @BeforeEach
    void setUp() {
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setConfirmPassword((user.getPassword()));
    }

    @Test
    void findAll() {
        given(userRepository.findAll()).willReturn(Collections.singletonList(user));

        assertThat(userService.findAll()).isEqualTo(Collections.singletonList(user));
    }

    @Test
    void 유저생성() {
        given(userRepository.existsByEmail(userDto.getEmail())).willReturn(false);

        assertDoesNotThrow(() -> userService.createNewUser(userDto));
    }

    @Test
    void 유저생성_실패_이메일_중복() {
        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

        assertThrows(NotValidUserInfoException.class, () -> userService.createNewUser(userDto));
    }

    @Test
    void 유저_삭제() {
        given(httpSession.getAttribute("user")).willReturn(user);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(user.getEmail()));
    }

    @Test
    void 유저_삭제_실패() {
        given(httpSession.getAttribute("user")).willReturn(user);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> userService.deleteUser(user.getEmail()));
    }

}
