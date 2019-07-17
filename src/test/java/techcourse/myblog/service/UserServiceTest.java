package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.DuplicatedUserException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        userService.save(user);
    }

    @Test
    void 회원정보_등록시_예외처리() {
        User duplicatedUser = User.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }
}