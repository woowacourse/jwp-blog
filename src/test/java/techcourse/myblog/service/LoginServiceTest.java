package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.dto.AuthenticationDto;
import techcourse.myblog.service.exception.LoginException;

import java.util.Optional;

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
        authenticationDto.setEmail(user.getEmail());
        authenticationDto.setPassword(user.getPassword());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertEquals(loginService.login(authenticationDto), user);
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
