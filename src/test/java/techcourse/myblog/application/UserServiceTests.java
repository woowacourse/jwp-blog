package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.LoginException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.support.encrytor.EncryptHelper;

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

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EncryptHelper encryptHelper;

    private static final UserRequest USER_REQUEST_1 = new UserRequest("amo", "PassWord123!", "amo@woowahan.com");
    private static final UserRequest USER_REQUEST_2 = new UserRequest("bmo", "PassWord123!", "bmo@woowahan.com");
    private static final LoginRequest LOGIN_REQUEST = new LoginRequest("amo@woowahan.com", "PassWord123!");

    private static final User USER_1 = new User("amo", "PassWord123!", "amo@woowahan.com");
    private static final User USER_2 = new User("bmo", "PassWord123!", "bmo@woowahan.com");
    private static final User USER_3 = new User("cmo", "PassWord123!", "cmo@woowahan.com");
    private InOrder inOrder;

    @BeforeEach
    void setup() {
        inOrder = inOrder(userRepository);
    }

    @Test
    void save() {
        userService.saveUser(USER_REQUEST_1);
        userService.saveUser(USER_REQUEST_2);

        verify(userRepository, times(1)).save(USER_1);
        verify(userRepository, times(1)).save(USER_2);
        verify(userRepository, never()).save(USER_3);

        inOrder.verify(userRepository).save(USER_1);
        inOrder.verify(userRepository).save(USER_2);

    }

    @Test
    void update() {
        given(userRepository.findUserByEmail(USER_REQUEST_2.getEmail())).willReturn(Optional.of(USER_1));
        userService.editUserName(USER_1.getId(), USER_REQUEST_2.getName());
        verify(userRepository, times(1)).findById(USER_1.getId());
        assertThat(USER_1.getName()).isEqualTo(USER_REQUEST_2.getName());
    }

    @Test
    void 존재하지_않는_ID로_업데이트시_Exception() {
        assertThrows(NoUserException.class, () -> userService.editUserName(USER_1.getId(), USER_REQUEST_2.getName()));
    }

    @Test
    void checkLogin() {
        given(userRepository.findUserByEmail(USER_REQUEST_1.getEmail())).willReturn(Optional.of(USER_1));
        UserResponse userDto = userService.checkLogin(LOGIN_REQUEST);

        assertThat(userDto).isEqualTo(USER_REQUEST_1);

        verify(userRepository, times(1)).findUserByEmail(USER_REQUEST_1.getEmail());
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

        assertThat(userResponses.get(0).getId()).isEqualTo(USER_1.getId());
        assertThat(userResponses.get(1).getId()).isEqualTo(USER_2.getId());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void delete() {
        userService.deleteById(USER_1.getId());
        verify(userRepository, times(1)).deleteById(USER_1.getId());
    }
}