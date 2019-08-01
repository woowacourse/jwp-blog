package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private InOrder inOrder;

    String email;
    String name;
    String password;

    UserDto userDto;
    LoginDto loginDto;
    User user;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(userRepository);
        UserConverter userConverter = UserConverter.getInstance();

        email = "zino@naver.com";
        name = "zino";
        password = "zinozino";

        userDto = new UserDto(email, name, password);
        loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);
        user = userConverter.convertFromDto(userDto);
    }

    @Test
    void 중복되지_않은_User_생성() {
        userService.save(userDto);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void 중복된_User_생성_예외발생() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        userRepository.save(user);
        userRepository.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void 저장된_User_조회() {
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        assertThat(userService.findByEmail(userDto.getEmail())).isEqualTo(userDto);
    }

    @Test
    void 저장되지_않은_User_조회_예외발생() {
        String email = "zino1@naver.com";

        assertThrows(NotExistUserIdException.class, () -> userService.findByEmail(email));
    }

    @Test
    void 저장된_User_삭제() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        userService.removeById(userDto, userDto.getEmail());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void 저장된_id_login() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.login(loginDto));
    }

    @Test
    void 저장되지_않은_id_login() {
        loginDto.setEmail("bimo@hi.com");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(NotExistUserIdException.class, () -> userService.login(loginDto));
    }

    @Test
    void 비밀번호_불일치_login_예외발생() {
        loginDto.setPassword("123123123");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(NotMatchPasswordException.class, () -> userService.login(loginDto));

    }
}
