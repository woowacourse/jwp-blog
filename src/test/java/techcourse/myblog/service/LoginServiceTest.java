package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import techcourse.myblog.domain.dto.UserDto;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.domain.dto.AuthenticationDto;
import techcourse.myblog.domain.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.domain.user.UserTest.user;

@SpringBootTest
public class LoginServiceTest {

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    private AuthenticationDto authenticationDto = new AuthenticationDto();

    @Test
    void 로그인_성공() {
        UserDto userDto = new UserDto("heejoo", "heejoo@gmail.com");

        authenticationDto.setEmail(user.getEmail());
        authenticationDto.setPassword(user.getPassword());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        LoginUser loginUser = loginService.login(authenticationDto);

        assertThat(userDto.getEmail()).isEqualTo(loginUser.getEmail());
        assertThat(userDto.getUserName()).isEqualTo(loginUser.getUserName());
    }

    @Test
    void 로그인_이메일_실패() {
        authenticationDto.setEmail("error@gmail.com");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(LoginException.class, () -> loginService.login(authenticationDto));
    }

    @Test
    void 로그인_패스워드_실패() {
        authenticationDto.setEmail(user.getEmail());
        authenticationDto.setPassword("error");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(LoginException.class, () -> loginService.login(authenticationDto));
    }
}
