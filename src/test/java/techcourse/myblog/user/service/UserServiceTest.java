package techcourse.myblog.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.SignUpException;
import techcourse.myblog.user.repository.UserRepository;
import techcourse.myblog.utils.converter.UserConverter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static final String NAME = "코니코니";
    private static final String EMAIL = "cony@naver.com";
    private static final String PASSWORD = "@Password1234";

    private User user;
    private UserRequestDto userRequestDto;

    @Autowired
    private UserService userService;

    @MockBean(name = "userRepository") // 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User(NAME, PASSWORD, EMAIL);
        userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(EMAIL);
        userRequestDto.setName(NAME);
        userRequestDto.setPassword(PASSWORD);
    }

    @Test
    public void 중복된_이메일을_등록하는_경우_예외처리() {
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        // then
        assertThrows(SignUpException.class, () -> userService.addUser(userRequestDto));
    }

    @Test
    public void 회원정보가_정상적으로_수정되는지_테스트() {
        // given
        String changedName = "NameHas";
        String changedPassword = "PasswordHasChanged1TIME!";
        userRequestDto.setName(changedName);
        userRequestDto.setPassword(changedPassword);

        User changedUser = UserConverter.toEntity(this.userRequestDto);

        when(userRepository.save(this.user)).thenReturn(this.user);
        when(userRepository.save(changedUser)).thenReturn(changedUser);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(changedUser));

        // when
        UserResponseDto userResponseDto = userService.updateUser(userRequestDto, UserConverter.toResponseDto(changedUser));

        // then
        assertThat(userResponseDto.getName()).isEqualTo(changedName);
    }

    @Test
    public void 존재하지_않는_회원정보를_수정하는_경우_예외처리() {
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.updateUser(userRequestDto, UserConverter.toResponseDto(this.user)))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    public void 회원이_정상적으로_탈퇴되는지_테스트() {
        // given
        UserResponseDto userResponseDto = UserConverter.toResponseDto(this.user);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(this.user));
        doNothing().when(userRepository).delete(this.user);

        // when
        userService.deleteUser(userResponseDto);

        // then
        verify(userRepository, times(1)).delete(this.user);
    }

    @Test
    public void 존재하지_않은_회원이_탈퇴하는_경우_예외처리() {
        // given
        UserResponseDto userResponseDto = UserConverter.toResponseDto(this.user);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.deleteUser(userResponseDto))
                .isInstanceOf(NotFoundUserException.class);
    }
}