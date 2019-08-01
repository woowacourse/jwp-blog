package techcourse.myblog.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;

    @Autowired
    private UserService userService;

    private UserResponse defaultUser;

    @BeforeEach
    void setUp() {
        defaultUser = userService.findById(DEFAULT_USER_ID);
    }

    @Test
    void 사용자_생성_확인() {
        UserResponse persistUser = userService.save(new UserRequest("john@example.com", "john", "p@ssw0rd"));
        assertThat(persistUser).isEqualTo(new UserResponse(persistUser.getId(), "john@example.com", "john"));
    }

    @Test
    void 사용자_생성_오류확인_사용자요청dto가_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.save(null));
    }

    @Test
    void 사용자_생성_오류확인_이미_존재하는_사용자일_경우() {
        assertThatExceptionOfType(DuplicatedEmailException.class)
                .isThrownBy(() -> userService.save(new UserRequest(defaultUser.getEmail(), defaultUser.getName(), "p@ssw0rd")));
    }

    @Test
    void 사용자_목록_조회_확인() {
        assertThat(userService.findAll()).hasSize(2);
    }

    @Test
    void 사용자_정보_수정_확인() {
        UserResponse updateUser = userService.update(defaultUser.getEmail(), "dowon");
        assertThat(updateUser).isEqualTo(new UserResponse(updateUser.getId(), defaultUser.getEmail(), "dowon"));
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
        UserResponse userToDelete = userService.save(new UserRequest("done@naver.com", "done", "12345678"));
        userService.delete(userToDelete);
        assertThat(userService.findAll()).doesNotContain(userToDelete);
    }

    @Test
    void 사용자_정보_삭제_오류확인_사용자가_없을_경우() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.delete(new UserResponse(defaultUser.getId() - 1, "done@naver.com", "done")));
    }
}
