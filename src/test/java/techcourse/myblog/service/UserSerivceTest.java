package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSerivceTest {
    private static final String TEST_EMAIL_1 = "test1@test.com";
    private static final String TEST_EMAIL_2 = "test2@test.com";
    private static final String TEST_PASSWORD_1 = "!Q@W3e4r";
    private static final String TEST_USERNAME = "test1";
    private static final UserDto userDTO = new UserDto(TEST_USERNAME, TEST_EMAIL_1, TEST_PASSWORD_1);

    private User user;

    private UserService userService;

    @Autowired
    public UserSerivceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        user = userService.save(userDTO);
    }

    @Test
    void 유저_저장_테스트() {
        assertThat(userService.getUsers().size()).isEqualTo(1);
    }

    @Test
    void 이메일로_중복인지_테스트() {
        UserDto userDTOTest = new UserDto(TEST_USERNAME, TEST_EMAIL_1, TEST_PASSWORD_1);
        assertThat(userService.isDuplicateEmail(userDTOTest)).isTrue();
    }

    @Test
    void 이메일로_중복아닌지_테스트() {
        UserDto userDTOTest = new UserDto(TEST_USERNAME, TEST_EMAIL_2, TEST_PASSWORD_1);
        assertThat(userService.isDuplicateEmail(userDTOTest)).isFalse();
    }

    @AfterEach
    void tearDown() {
        userService.delete(user);
    }
}
