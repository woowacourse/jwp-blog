package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserSerivceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_PASSWORD_2 = "!Q@W3e4r5t";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_USERNAME_2 = "test2";
    private static final UserDTO USER_DTO_1 = new UserDTO(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final UserDTO USER_DTO_2 = new UserDTO(TEST_USERNAME_2, TEST_EMAIL_2, TEST_PASSWORD_2);

    private static final User USER_1 = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final User USER_2 = new User(TEST_USERNAME_2, TEST_EMAIL_2, TEST_PASSWORD_2);
    private static final User USER_3 = new User("test3", "test3@test.com", "QW!@ER#$");

    private static final LoginDTO LOGIN = new LoginDTO(TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final LoginDTO WRONG_LOGIN_ID = new LoginDTO(TEST_EMAIL_2, TEST_PASSWORD_1);
    private static final LoginDTO WRONG_PASSWORD_ID = new LoginDTO(TEST_EMAIL_1, TEST_PASSWORD_2);

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(userRepository);
    }

    @Test
    void 유저_저장_테스트() {
        userService.save(USER_DTO_1);
        userService.save(USER_DTO_2);

        verify(userRepository, atLeastOnce()).save(USER_1);
        verify(userRepository, atLeastOnce()).save(USER_2);
        verify(userRepository, times(1)).save(USER_1);
        verify(userRepository, times(1)).save(USER_2);
        verify(userRepository, never()).save(USER_3);

        inOrder.verify(userRepository).save(USER_1);
        inOrder.verify(userRepository).save(USER_2);
    }

    @Test
    void 이메일로_중복인지_테스트() {
        userService.save(USER_DTO_1);
        userService.save(USER_DTO_1);
        verify(userRepository, atLeast(2)).save(USER_1);

        given(userRepository.existsByEmail(TEST_EMAIL_1)).willReturn(true);
        assertThrows(EmailRepetitionException.class, () -> userService.save(USER_DTO_1));
    }

    @Test
    void 로그인_성공_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        assertThat(userService.getLoginUser(LOGIN).getEmail()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    void 로그인_아이디_찾기_실패_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(null);

        assertThrows(UserNotExistException.class, () -> {
            userService.getLoginUser(WRONG_LOGIN_ID);
            verify(userRepository, atLeastOnce()).findByEmail(WRONG_LOGIN_ID.getEmail());
        });
    }

    @Test
    void 로그인_패스워드_불일치_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        assertThrows(LoginFailException.class, () -> {
            userService.getLoginUser(WRONG_PASSWORD_ID);
            verify(userRepository, atLeastOnce()).findByEmail(WRONG_PASSWORD_ID.getEmail());
        });
    }

    @Test
    void 유저_정보_수정_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        User updateUser = userService.update(new UserDTO(TEST_USERNAME_2, TEST_EMAIL_1, TEST_PASSWORD_2));
        verify(userRepository, atLeastOnce()).findByEmail(TEST_EMAIL_1);

        assertThat(updateUser.getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(updateUser.getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @Test
    void 유저_삭제_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        userService.delete(USER_1);
        verify(userRepository, atLeastOnce()).findByEmail(TEST_EMAIL_1);

        assertThat(userService.getUsers().size()).isEqualTo(0);
    }
}
