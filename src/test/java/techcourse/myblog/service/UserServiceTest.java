package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.NoSuchUserException;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static final long DEFAULT_USER_ID = 0L;
    private static final String NAME = "코니코니";
    private static final String EMAIL = "cony@naver.com";
    private static final String PASSWORD = "@Password1234";

    private User user;

    @Autowired
    private UserService userService;

    @MockBean(name = "userRepository") // 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User(DEFAULT_USER_ID, NAME, EMAIL, PASSWORD);
    }

    @Test
    public void 중복된_이메일을_등록하는_경우_예외처리() {
        // given
        when(userRepository.countByEmail(EMAIL)).thenReturn(1L);

        // then
        assertThrows(DuplicateUserException.class, () -> userService.createUser(UserAssembler.writeDto(this.user)));
    }

    @Test
    public void 등록된_사용자가_로그인을_하는_경우_테스트() {
        // given
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(this.user));

        // when
        User user = userService.findUserByEmail(EMAIL);

        // then
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void 등록되지_않은_사용자가_로그인을_하는_경우_예외처리() {
        assertThrows(NoSuchUserException.class, () -> userService.findUserByEmail(EMAIL));
    }

    @Test
    public void 회원정보가_정상적으로_수정되는지_테스트() {
        // given
        String changedName = "NameHasChanged";
        UserDto userDto = UserAssembler.writeDto(this.user);
        userDto.setName(changedName);
        User changedUser = UserAssembler.writeUser(userDto);

        when(userRepository.save(this.user)).thenReturn(this.user);
        when(userRepository.save(changedUser)).thenReturn(changedUser);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(changedUser));

        // when
        User userResult = userService.updateUser(userDto);

        // then
        assertThat(userResult.getName()).isEqualTo(changedName);
    }

    @Test
    public void 존재하지_않는_회원정보를_수정하는_경우_예외처리() {
        // given
        UserDto givenUserDto = UserAssembler.writeDto(this.user);
        when(userRepository.save(this.user)).thenReturn(null);

        // then
        assertThrows(NoSuchUserException.class, () -> userService.updateUser(givenUserDto));
    }

    @Test
    public void 회원이_정상적으로_탈퇴되는지_테스트() {
        // given
        doNothing().when(userRepository).deleteById(this.user.getUserId());

        // when
        userService.deleteUser(this.user.getUserId());

        // then
        verify(userRepository, times(1)).deleteById(this.user.getUserId());
    }
}