package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class userServiceTest {

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;

    @Autowired
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("buddy@gmail.com");
        userDto.setUserName("Buddy");
        userDto.setPassword("Aa12345!");
        userDto.setConfirmPassword("Aa12345!");
    }

    @Test
    void findAll() {
        User user = userDto.toEntity();
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        assertThat(userService.findAll()).isEqualTo(Arrays.asList(user));
    }

    @Test
    void 유저생성() {
        given(userRepository.existsByEmail(userDto.getEmail())).willReturn(false);
        assertDoesNotThrow(() -> userService.createNewUser(userDto));
    }

    @Test
    void 유저생성_실패_이메일_중복() {
        given(userRepository.existsByEmail(userDto.getEmail())).willReturn(true);

        assertThrows(NotValidUserInfoException.class, () -> userService.createNewUser(userDto));
    }

    @Test
    void 유저_삭제() {
        User user = userDto.toEntity();
        String email = user.getEmail();
        given(httpSession.getAttribute("user")).willReturn(user);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(httpSession));
    }

    @Test
    void 유저_삭제_실패() {
        User user = userDto.toEntity();
        String email = user.getEmail();
        given(httpSession.getAttribute("user")).willReturn(user);
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> userService.deleteUser(httpSession));
    }

}
