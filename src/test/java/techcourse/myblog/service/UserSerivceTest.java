package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSerivceTest {
    private static final UserDTO userDTO = new UserDTO("test1","test1@testtest.com", "1234");

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userService.save(userDTO);
    }

    @Test
    void 유저_저장_테스트() {
        assertThat(userService.getUsers().size()).isEqualTo(1);
    }

    @Test
    void 이메일로_중복인지_테스트() {
        UserDTO userDTOTest = new UserDTO("test2","test1@testtest.com", "12345");
        assertThat(userService.isDuplicateEmail(userDTOTest)).isTrue();
    }

    @Test
    void 이메일로_중복아닌지_테스트() {
        UserDTO userDTOTest = new UserDTO("test2","test2@testtest.com", "12345");
        assertThat(userService.isDuplicateEmail(userDTOTest)).isFalse();
    }

    @AfterEach
    void tearDown() {
        userService.delete(userDTO);
    }
}
