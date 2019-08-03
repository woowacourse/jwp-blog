package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.exception.NoRowException;
import techcourse.myblog.web.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserService userService;
    private User beforeUser;

    @BeforeEach
    void setUp() {
        beforeUser = userService.findByEmail("aiden@woowa.com");
    }

    @Test
    void 사용자_조회() {
        assertThat(userService.findByEmail(beforeUser.getEmail())).isEqualTo(beforeUser);
    }

    @Test
    void 사용자_저장() {
        User newUser = userService.save(
                new UserDto("whale", "whale@naver.com", "whale2")
        );
        assertThat(userService.findByEmail(newUser.getEmail())).isEqualTo(newUser);
    }

    @Test
    void 사용자_수정() {
        User updatedUser = userService.update(
                new UserDto("pobi", beforeUser.getEmail(), beforeUser.getPassword()),
                beforeUser
        );
        assertThat(userService.findByEmail(beforeUser.getEmail())).isEqualTo(updatedUser);
    }

    @Test
    void 사용자_삭제() {
        beforeUser = userService.findByEmail("woowa@woowa.com");
        userService.remove(beforeUser.getEmail());
        assertThatThrownBy(() -> userService.findByEmail(beforeUser.getEmail()))
                .isInstanceOf(NoRowException.class);
    }

    @Test
    void 존재_확인() {
        assertThat(userService.exists(beforeUser.getEmail())).isTrue();
    }

    @Test
    void 존재_안함() {
        assertThat(userService.exists("coogie@naver.com")).isFalse();
    }
}