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
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.Arrays;
import java.util.List;
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

    private static final UserConverter USER_CONVERTER = UserConverter.getInstance();

    private static final UserDto USER_DTO_1 = new UserDto("amo@woowahan.com", "amo", "amoamoamo");
    private static final UserDto USER_DTO_2 = new UserDto("bmo@woowahan.com", "bmo", "bmobmobmo");
    private static final UserDto USER_DTO_3 = new UserDto("cmo@woowahan.com", "cmo", "cmoamoamo");
    private static final LoginDto LOGIN_DTO = new LoginDto("amo@woowahan.com", "amoamoamo");

    private static final User USER_1 = USER_CONVERTER.convertFromDto(USER_DTO_1);
    private static final User USER_2 = USER_CONVERTER.convertFromDto(USER_DTO_2);
    private static final User USER_3 = USER_CONVERTER.convertFromDto(USER_DTO_3);
    private InOrder inOrder;

    @BeforeEach
    void setup() {
        inOrder = inOrder(userRepository);
    }

    @Test
    void save() {
        userService.save(USER_DTO_1);
        userService.save(USER_DTO_2);

        verify(userRepository, times(1)).save(USER_1);
        verify(userRepository, times(1)).save(USER_2);
        verify(userRepository, never()).save(USER_3);

        inOrder.verify(userRepository).save(USER_1);
        inOrder.verify(userRepository).save(USER_2);
    }

    @Test
    void save_중복된_ID_Exception() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));

        assertThrows(DuplicatedIdException.class, () -> userService.save(USER_DTO_1));
    }

    @Test
    void update() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        userService.modify(USER_DTO_1, USER_DTO_1.getEmail());
        verify(userRepository, times(1)).findById(USER_1.getEmail());
    }

    @Test
    void update_없는_ID_Exception() {
        assertThrows(NotExistUserIdException.class, () -> userService.modify(USER_DTO_1, USER_DTO_1.getEmail()));
    }

    @Test
    void findById() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        UserDto userDto = userService.findById(USER_1.getEmail());

        assertThat(userDto).isEqualTo(USER_DTO_1);

        verify(userRepository, times(1)).findById(USER_DTO_1.getEmail());
    }

    @Test
    void find_없는_ID_Exception() {
        assertThrows(NotExistUserIdException.class, () -> userService.findById(USER_DTO_1.getEmail()));
    }

    @Test
    void findAll() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        given(userRepository.findById(USER_DTO_2.getEmail())).willReturn(Optional.of(USER_2));

        List<User> users = Arrays.asList(USER_1, USER_2);

        given(userRepository.findAll()).willReturn(users);

        List<UserDto> userDtos = userService.findAll();

        assertThat(userDtos.get(0)).isEqualTo(USER_CONVERTER.convertFromEntity(USER_1));
        assertThat(userDtos.get(1)).isEqualTo(USER_CONVERTER.convertFromEntity(USER_2));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void delete() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        userService.removeById(USER_DTO_1, USER_DTO_1.getEmail());
        verify(userRepository, times(1)).deleteById(USER_1.getEmail());
    }

    @Test
    void delete_없는_ID_Exception() {
        assertThrows(NotExistUserIdException.class, () -> userService.removeById(USER_DTO_1, USER_DTO_1.getEmail()));
    }

    @Test
    void login() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        assertDoesNotThrow(() -> userService.login(LOGIN_DTO));
    }

    @Test
    void login_없는_ID_Exception() {
        assertThrows(NotExistUserIdException.class, () -> userService.login(LOGIN_DTO));
    }

    @Test
    void login_비밀번호_불일치_Exception() {
        given(userRepository.findById(USER_DTO_1.getEmail())).willReturn(Optional.of(USER_1));
        LoginDto loginDto = new LoginDto(LOGIN_DTO.getEmail(), LOGIN_DTO.getPassword() + "123");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(loginDto));
    }
}
