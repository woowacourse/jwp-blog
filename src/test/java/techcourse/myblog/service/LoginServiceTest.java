package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTest {
    private static final long DEFAULT_USER_ID = 0L;
    private static final String NAME = "코니코니";
    private static final String EMAIL = "cony@naver.com";
    private static final String PASSWORD = "@Password1234";

    private User user = new User(DEFAULT_USER_ID, NAME, EMAIL, PASSWORD);

    @Autowired
    private LoginService loginService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Test
    public void 정상적으로_로그인이_성공하는지_테스트() {
        // given
        UserDto userDto = getLoggingInUserDto(EMAIL, PASSWORD);
        when(userRepository.countByEmail(EMAIL)).thenReturn(1L);
        when(userRepository.findUserByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.of(this.user));

        // when
        User user = loginService.findUserByEmailAndPassword(userDto);

        // then
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void 존재하지_않는_이메일로_로그인을_시도하는_경우_예외처리() {
        // given
        String wrongEmail = "wrongEmail";
        UserDto userDto = getLoggingInUserDto(wrongEmail, PASSWORD);
        when(userRepository.countByEmail(EMAIL)).thenReturn(1L);

        // then
        assertThrows(FailedLoginException.class, () -> loginService.findUserByEmailAndPassword(userDto));
    }

    @Test
    public void 틀린_비밀번호로_로그인을_시도하는_경우_예외처리() {
        // given
        String wrongPassword = "wrongPassword";
        UserDto userDto = getLoggingInUserDto(EMAIL, wrongPassword);
        when(userRepository.countByEmail(EMAIL)).thenReturn(1L);
        when(userRepository.findUserByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.of(this.user));

        // then
        assertThrows(FailedLoginException.class, () -> loginService.findUserByEmailAndPassword(userDto));
    }

    private UserDto getLoggingInUserDto(String email, String password) {
        UserDto userDto = new UserDto();

        userDto.setEmail(email);
        userDto.setPassword(password);

        return userDto;
    }
}
