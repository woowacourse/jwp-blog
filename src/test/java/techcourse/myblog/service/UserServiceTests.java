package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
        user = new User("name", "save@mail.com", "Passw0rd!");
    }

    @Test
    public void save_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.save(new UserDto("name", "e@mail.com", "Passw0rd!")));
        assertThrows(DuplicatedEmailException.class, () ->
                userService.save(new UserDto("name", user.getEmail(), "Passw0rd!")));
    }

    @Test
    public void login_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.login(new UserDto("", user.getEmail(), "Passw0rd!")));
        assertThrows(LoginFailedException.class, () ->
                userService.login(new UserDto("", "e@mail.com", "Passw0rd!")));
    }

    @Test
    public void findAll_test() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        userService.findAll().forEach(foundUser -> {
            compareUser(user, foundUser);
        });
    }

    @Test
    public void findByEmail_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        User foundUser = userService.findByEmail(user.getEmail());
        compareUser(foundUser, user);

        assertThrows(UnfoundUserException.class, () -> userService.findByEmail("e@email.com"));
    }

    @Test
    public void modify_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        UserDto userDto = new UserDto("nameee", user.getEmail(), user.getPassword());
        User changedUser = userService.modify(userDto);
        compareUser(changedUser, user);
    }

    private void compareUser(User user1, User user2) {
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}