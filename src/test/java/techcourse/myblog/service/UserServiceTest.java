package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.exception.EmailDuplicatedException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    static final String USER_NAME = "test1";
    static final String EMAIL = "test1@test.com";
    static final String PASSWORD = "!Q@W3e4r";

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private InOrder inOrder;
    private User testUser;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(userRepository);
        testUser = new User(USER_NAME, EMAIL, PASSWORD);
        userDto = new UserDto(USER_NAME, EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("User를 저장한다.")
    void saveUser() {
        User anotherUser = new User("another", "another@test.com", "QW!@ER#$");
        userService.save(userDto);

        verify(userRepository, atLeastOnce()).save(testUser);
        verify(userRepository, times(1)).save(testUser);
        verify(userRepository, never()).save(anotherUser);

        inOrder.verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("email이 이미 사용중인지 확인한다.")
    void checkEmailDuplication() {
        given(userRepository.save(testUser)).willThrow(new DataIntegrityViolationException("duplicate email"));

        assertThatThrownBy(() -> userService.save(userDto)).isInstanceOf(EmailDuplicatedException.class);
        verify(userRepository, atLeast(1)).save(testUser);
    }

    @Test
    @DisplayName("User의 정보를 수정한다.")
    void updateUser() {
        final String updatedName = "updated";
        final String updatedPassword = "updated!@QW12";

        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(testUser));

        User updateUser = userService.update(new UserDto(updatedName, EMAIL, updatedPassword));

        verify(userRepository, atLeastOnce()).findByEmail(EMAIL);
        assertThat(updateUser.getUserName()).isEqualTo(updatedName);
        assertThat(updateUser.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    @DisplayName("User를 삭제한다.")
    void deleteUser() {
        final Long testId = 1l;
        userService.delete(testId);

        verify(userRepository, atLeast(1)).deleteById(testId);
    }
}