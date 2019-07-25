package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTests {

    private final UserService userService;
    private final String NAME = "name";
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "Email123!";
    private User savedUser;

    @Autowired
    public UserServiceTests(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        savedUser = userService.save(new User(NAME, EMAIL, PASSWORD));
    }

    @Test
    void 중복_이메일_확인() {
        assertThat(userService.isDuplicatedEmail(EMAIL)).isTrue();
    }

    @Test
    void 중복하지_않는_이메일_확인() {
        assertThat(userService.isDuplicatedEmail("Wrong@email.com")).isFalse();
    }

    @Test
    void 회원_이름_수정() {
        String updatedName = "updated Name";
        User updatedUser = userService.updateName(savedUser.getId(), "updated Name");
        assertThat(updatedUser.getName()).isEqualTo(updatedName);
    }

    @Test
    void 전체_회원_조회_확인() {
        List<User> users = userService.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void ID로_USER_찾기_확인() {
        assertThat(userService.findUserById(savedUser.getId())).isEqualTo(savedUser);
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(savedUser.getId());
    }
}
