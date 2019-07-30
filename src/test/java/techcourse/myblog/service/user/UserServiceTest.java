package techcourse.myblog.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;

    @Autowired
    private UserService userService;

    private UserResponseDto defaultUser;

    @BeforeEach
    void setUp() {
        defaultUser = userService.findById(DEFAULT_USER_ID);
    }

    @Test
    void 사용자_생성_확인() {
        UserResponseDto persistUser = userService.save(new UserRequestDto("john@example.com", "john", "p@ssw0rd"));
        assertThat(persistUser).isEqualTo(new UserResponseDto(persistUser.getId(), "john@example.com", "john"));
    }

    @Test
    void 사용자_생성_오류확인_사용자요청dto가_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.save(null));
    }

    @Test
    void 사용자_생성_오류확인_이미_존재하는_사용자일_경우() {
        assertThatExceptionOfType(DuplicatedEmailException.class)
                .isThrownBy(() -> userService.save(new UserRequestDto(defaultUser.getEmail(), defaultUser.getName(), "p@ssw0rd")));
    }

    @Test
    void 사용자_목록_조회_확인() {
        assertThat(userService.findAll()).hasSize(2);
    }

    @Test
    void 사용자_정보_수정_확인() {
        UserResponseDto updateUser = userService.update(defaultUser.getEmail(), "dowon");
        assertThat(updateUser).isEqualTo(new UserResponseDto(updateUser.getId(), defaultUser.getEmail(), "dowon"));
    }

    @Test
    void 사용자_정보_수정_오류확인_이메일이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.update(null, "dowon"));
    }

    @Test
    void 사용자_정보_수정_오류확인_이름이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.update(defaultUser.getEmail(), null));
    }

    @Test
    void 사용자_정보_수정_오류확인_사용자가_없을_경우() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.update("done@naver.com", "dowon"));
    }

    @Test
    void 사용자_정보_삭제_확인() {
        UserResponseDto userToDelete = userService.save(new UserRequestDto("done@naver.com", "done", "12345678"));
        userService.delete(userToDelete);
        assertThat(userService.findAll()).doesNotContain(userToDelete);
    }

    @Test
    void 사용자_정보_삭제_오류확인_사용자가_없을_경우() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.delete(new UserResponseDto(defaultUser.getId() - 1, "done@naver.com", "done")));
    }
}
