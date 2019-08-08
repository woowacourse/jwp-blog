package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.web.dto.UserResponse;
import techcourse.myblog.application.exception.LoginException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.support.encrytor.PasswordBCryptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static final UserRequest USER_REQUEST_1 = new UserRequest("amo", "amo@woowahan.com", "PassWord123!");
    private static final UserRequest USER_REQUEST_2 = new UserRequest("bmo", "bmo@woowahan.com", "PassWord123!");
    private static final LoginRequest LOGIN_REQUEST = new LoginRequest("amo@woowahan.com", "PassWord123!");

    private static final Long USER_1_ID = 100L;
    private static final User USER_1 = spy(new User("amo", new PasswordBCryptor().encrypt("PassWord123!"), "amo@woowahan.com"));
    private static final User USER_2 = spy(new User("bmo", new PasswordBCryptor().encrypt("PassWord123!"), "bmo@woowahan.com"));
    private static final User USER_3 = spy(new User("cmo", new PasswordBCryptor().encrypt("PassWord123!"), "cmo@woowahan.com"));
    private InOrder inOrder;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, new PasswordBCryptor());
        inOrder = inOrder(userRepository);
    }

    @Test
    void save() {
        userService.saveUser(USER_REQUEST_1);
        userService.saveUser(USER_REQUEST_2);

        verify(userRepository, times(2)).save(any());
    }

    @Test
    void update() {
        doReturn(USER_1_ID).when(USER_1).getId();
        doReturn(Optional.of(USER_1)).when(userRepository).findById(USER_1_ID);
        given(userRepository.findByEmail(USER_REQUEST_2.getEmail())).willReturn(Optional.of(USER_1));
        userService.update(USER_1.getId(), USER_REQUEST_2.getName());
        verify(userRepository, times(1)).findById(USER_1.getId());
        assertThat(USER_1.getName()).isEqualTo(USER_REQUEST_2.getName());
    }

    @Test
    void 존재하지_않는_ID로_업데이트시_Exception() {
        assertThrows(NoUserException.class, () -> userService.update(USER_1.getId(), USER_REQUEST_2.getName()));
    }

    @Test
    void checkLogin() {
        given(userRepository.findByEmail(USER_REQUEST_1.getEmail())).willReturn(Optional.of(USER_1));
        UserResponse userDto = userService.checkLogin(LOGIN_REQUEST);

        assertThat(userDto.getEmail()).isEqualTo(USER_REQUEST_1.getEmail());

        verify(userRepository, times(1)).findByEmail(USER_REQUEST_1.getEmail());
    }

    @Test
    void 존재하지_않는_ID로_find시_Exception() {
        assertThrows(LoginException.class, () -> userService.checkLogin(LOGIN_REQUEST));
    }

    @Test
    void findAll() {
        List<User> users = Arrays.asList(USER_1, USER_2);

        given(userRepository.findAll()).willReturn(users);

        List<UserResponse> userResponses = userService.findAll();
        assertThat(userResponses).hasSize(2);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void delete() {
        userService.deleteById(USER_1.getId());
        verify(userRepository, times(1)).deleteById(USER_1.getId());
    }
}