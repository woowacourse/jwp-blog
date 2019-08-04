package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserAssembler;
import techcourse.myblog.dto.user.SignUpRequest;
import techcourse.myblog.dto.user.UpdateUserRequest;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.user.SignUpException;
import techcourse.myblog.exception.user.UserException;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static final String NAME = "ike";
    private static final String PASSWORD = "Password!1";
    private static final String EMAIL = "ike@gmail.com";

    private static final String NAME_2 = "bob";
    private static final String EMAIL_2 = "bob@gmail.com";

    private User user;
    private SignUpRequest signUpRequestDto;
    private UpdateUserRequest updateUserRequestDto;

    @Autowired
    private UserService userService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User(NAME, PASSWORD, EMAIL);
        signUpRequestDto = new SignUpRequest(NAME, PASSWORD, EMAIL);
        updateUserRequestDto = new UpdateUserRequest(NAME_2, EMAIL_2);
    }

    @Test
    public void 중복된_이메일을_회원가입하는_경우_실패_테스트() {
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        // then
        assertThrows(SignUpException.class, () -> userService.addUser(signUpRequestDto));
    }

    @Test
    public void 회원정보가_정상적으로_수정되는지_테스트() {
        // given
        User changedUser = new User(NAME_2, PASSWORD, EMAIL_2);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(changedUser)).thenReturn(changedUser);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(changedUser));

        // when
        UserResponse userResponse = userService.updateUser(updateUserRequestDto, UserAssembler.toDto(user));

        // then
        assertThat(userResponse.getName()).isEqualTo(NAME_2);
    }

    @Test
    public void 존재하지_않는_회원정보를_수정하는_경우_실패_테스트() {
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.updateUser(updateUserRequestDto, UserAssembler.toDto(user)))
                .isInstanceOf(UserException.class);
    }

    @Test
    public void 회원이_정상적으로_탈퇴되는지_테스트() {
        // given
        UserResponse userResponse = UserAssembler.toDto(user);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // when
        userService.deleteUser(userResponse);

        // then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void 존재하지_않은_회원이_탈퇴하는_경우_실패_테스트() {
        // given
        UserResponse userResponse = UserAssembler.toDto(user);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.deleteUser(userResponse))
                .isInstanceOf(UserException.class);
    }
}