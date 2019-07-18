package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void 중복된_이메일을_등록하는_경우_예외처리() {
        User user1 = new User(0L, "코니코니", "cony@naver.com", "@Password1234");
        User user2 = new User(0L, "코니코니", "cony@naver.com", "@Password1234");

        userService.createUser(UserAssembler.writeDto(user1));

        assertThrows(DuplicateKeyException.class,
                () -> userService.createUser(UserAssembler.writeDto(user2)));
    }

    @Test
    public void 등록된_사용자가_로그인을_하는_경우_테스트() {
        User user = new User(0L, "에헴", "abc@abc.com", "@Password1234");
        userService.createUser(UserAssembler.writeDto(user));

        assertThat(userService.getUser(UserAssembler.writeDto(user)).getEmail()).isEqualTo(user.getEmail());
        assertThat(userService.getUser(UserAssembler.writeDto(user)).getName()).isEqualTo(user.getName());
        assertThat(userService.getUser(UserAssembler.writeDto(user)).getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void 등록되지_않은_사용자가_로그인을_하는_경우_예외처리() {
        User user = new User(0L, "에헴", "abcd@abcd.com", "@Password1234");

        assertThrows(NoSuchElementException.class, () -> {
            userService.getUser(UserAssembler.writeDto(user));
        });
    }
}