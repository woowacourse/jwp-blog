package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;

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

}