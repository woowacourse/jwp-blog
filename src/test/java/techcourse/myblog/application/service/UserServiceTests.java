package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTests {
    private static final String EXIST_EMAIL = "zino@naver.com";
    private static final String NOT_EXIST_EMAIL = "zino1@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "zinozino";
    private static final UserDto EXIST_USER_DTO = new UserDto(EXIST_EMAIL, NAME, PASSWORD);
    private static final UserDto NOT_EXIST_USER_DTO = new UserDto(NOT_EXIST_EMAIL, NAME, PASSWORD);

    private final UserService userService;

    @Mock
    private UserRepository userRepository;

    private UserServiceTests() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
        initUserRepositoryMock();
    }

    private void initUserRepositoryMock() {
        User existUser = new User(EXIST_EMAIL, NAME, PASSWORD);
        User notExistUser = new User(NOT_EXIST_EMAIL, NAME, PASSWORD);

        when(userRepository.findByEmail(EXIST_EMAIL)).thenReturn(Optional.of(existUser));
        when(userRepository.findByEmail(NOT_EXIST_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(notExistUser)).thenReturn(notExistUser);
        Mockito.doNothing().when(userRepository).deleteByEmail(EXIST_EMAIL);
    }


    @Test
    void 중복되지_않은_User_생성() {
        assertDoesNotThrow(() -> userService.save(NOT_EXIST_USER_DTO));
    }

    @Test
    void 중복된_User_생성_예외발생() {
        assertThrows(DuplicatedIdException.class, () -> userService.save(EXIST_USER_DTO));
    }

    @Test
    void 저장된_User_조회() {
        UserDto foundUser = userService.findByEmail(EXIST_EMAIL);

        assertThat(foundUser.getEmail()).isEqualTo(EXIST_EMAIL);
        assertThat(foundUser.getName()).isEqualTo(NAME);
        assertThat(foundUser.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void 저장되지_않은_User_조회_예외발생() {
        assertThrows(NotExistUserIdException.class, () -> userService.findByEmail(NOT_EXIST_EMAIL));
    }

    @Test
    void 저장된_User_삭제() {
        assertDoesNotThrow(() -> userService.removeByEmail(EXIST_EMAIL));
    }

    @Test
    void 저장된_id_login() {
        assertDoesNotThrow(() -> userService.login(LoginDto.of(EXIST_USER_DTO)));
    }

    @Test
    void 저장되지_않은_id_login() {
        assertThrows(NotExistUserIdException.class, () -> userService.login(LoginDto.of(NOT_EXIST_USER_DTO)));
    }

    @Test
    void 비밀번호_불일치_login_예외발생() {
        UserDto userDto = new UserDto(EXIST_EMAIL, NAME, PASSWORD + "123");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(LoginDto.of(userDto)));
    }
}
