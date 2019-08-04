package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.exception.EmailDuplicatedException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_PASSWORD_2 = "!Q@W3e4r5t";
    private static final String TEST_USERNAME_1 = "test1";
    private static final String TEST_USERNAME_2 = "test2";

    private static final User USER_1 = new User(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final User USER_2 = new User(TEST_USERNAME_2, TEST_EMAIL_2, TEST_PASSWORD_2);
    private static final User USER_3 = new User("test3", "test3@test.com", "QW!@ER#$");
    private static final UserDto USER_DTO_1 = new UserDto(TEST_USERNAME_1, TEST_EMAIL_1, TEST_PASSWORD_1);
    private static final UserDto USER_DTO_2 = new UserDto(TEST_USERNAME_2, TEST_EMAIL_2, TEST_PASSWORD_2);

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
        assertThrows(EmailDuplicatedException.class, () -> userService.save(USER_DTO_1));
    }


    @Test
    void 유저_정보_수정_테스트() {
        userService.save(USER_DTO_1);
        given(userRepository.findByEmail(TEST_EMAIL_1)).willReturn(Optional.of(USER_1));

        User updateUser = userService.update(new UserDto(TEST_USERNAME_2, TEST_EMAIL_1, TEST_PASSWORD_2));
        verify(userRepository, atLeastOnce()).findByEmail(TEST_EMAIL_1);

        assertThat(updateUser.getUserName()).isEqualTo(TEST_USERNAME_2);
        assertThat(updateUser.getPassword()).isEqualTo(TEST_PASSWORD_2);
    }

    @Test
    void 유저_삭제_테스트() {
        Long id = userService.save(USER_DTO_1);
        userService.delete(id);

        inOrder.verify(userRepository).save(USER_1);
        inOrder.verify(userRepository).deleteById(id);
        assertThat(userService.getUsers().size()).isEqualTo(0);
    }
}