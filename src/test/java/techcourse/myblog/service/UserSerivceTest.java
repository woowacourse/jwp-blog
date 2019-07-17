package techcourse.myblog.service;

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

    @Autowired
    UserService userService;

    @Test
    void 유저_저장_테스트() {
        UserDTO userDTO = new UserDTO("test1","test1@testtest.com", "1234");
        userService.save(userDTO);
        assertThat(userService.getUsers().size()).isEqualTo(1);
    }
}
