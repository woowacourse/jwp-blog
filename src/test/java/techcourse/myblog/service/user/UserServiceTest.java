package techcourse.myblog.service.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.user.UserRequestDto;
import techcourse.myblog.dto.user.UserResponseDto;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.EmailNotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private UserResponseDto persistUser;
    private String email;
    private String name;
    private String password;

    @BeforeEach
    void setUp() {
        email = "done@gmail.com";
        name = "done";
        password = "12345678";
        persistUser = userService.save(new UserRequestDto(email, name, password));
    }

    @AfterEach
    void tearDown() {
        userService.delete(persistUser);
    }

    @Test
    void 사용자_생성_확인() {
        assertThat(persistUser).isEqualTo(new UserResponseDto(email, name));
    }

    @Test
    void 사용자_생성_오류확인_사용자요청dto가_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.save(null));
    }

    @Test
    void 사용자_생성_오류확인_이미_존재하는_사용자일_경우() {
        assertThatExceptionOfType(DuplicatedEmailException.class)
                .isThrownBy(() -> userService.save(new UserRequestDto(email, name, password)));
    }

    @Test
    void 사용자_목록_조회_확인() {
        assertThat(Arrays.asList(persistUser)).isEqualTo(userService.findAll());
    }

    @Test
    void 사용자_정보_수정_확인() {
        UserResponseDto updateUser = userService.update(email, "dowon");
        assertThat(updateUser).isEqualTo(new UserResponseDto(email, "dowon"));
    }

    @Test
    void 사용자_정보_수정_오류확인_이메일이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.update(null, "dowon"));
    }

    @Test
    void 사용자_정보_수정_오류확인_이름이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.update(email, null));
    }

    @Test
    void 사용자_정보_수정_오류확인_사용자가_없을_경우() {
        assertThatExceptionOfType(EmailNotFoundException.class)
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
        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> userService.delete(new UserResponseDto("done@naver.com", "done")));
    }
}
