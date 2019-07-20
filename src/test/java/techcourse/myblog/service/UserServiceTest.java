package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.controller.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @Autowired
    private UserService userService;
    private User beforeUser;
    private static int flagNo = 1;

    @BeforeEach
    void setUp() {
        beforeUser = userService.save(
                new UserDto("aiden",
                        flagNo + "aiden1@naver.com",
                        "aiden3")
        );
        flagNo++;
    }

    @Test
    void findByEmail() {
        assertThat(userService.findByEmail(beforeUser.getEmail())).isEqualTo(beforeUser);
    }

    @Test
    void save() {
        User newUser = userService.save(
                new UserDto("whale", "whale@naver.com", "whale2")
        );
        assertThat(userService.findByEmail(newUser.getEmail())).isEqualTo(newUser);
    }

    @Test
    void update() {
        User updatedUser = userService.update(new UserDto("pobi", beforeUser.getEmail(), beforeUser.getPassword()));
        assertThat(userService.findByEmail(beforeUser.getEmail())).isEqualTo(updatedUser);
    }

    @Test
    void remove() {
        userService.remove(beforeUser.getEmail());
        assertThatThrownBy(() -> userService.findByEmail(beforeUser.getEmail()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isExist_true() {
        assertThat(userService.exists(beforeUser.getEmail())).isTrue();
    }

    @Test
    void isExist_false() {
        assertThat(userService.exists("coogie@naver.com")).isFalse();
    }

    @Test
    void name() {
    }
}