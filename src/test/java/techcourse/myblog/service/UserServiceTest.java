package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import techcourse.myblog.dto.UserDto;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private UserDto userDto;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUserId(0L);
        userDto.setName("코니");
        userDto.setPassword("@Password12");
        userDto.setPasswordConfirm("@Password12");
    }

    @Test
    public void 중복된_이메일을_등록하는_경우_예외처리() {
        userDto.setEmail("cony1@cony.com");
        userService.createUser(userDto);

        assertThrows(DuplicateKeyException.class,
                () -> userService.createUser(userDto));
    }

    @Test
    public void 등록된_사용자가_로그인을_하는_경우_테스트() {
        userDto.setEmail("cony2@cony.com");
        userService.createUser(userDto);

        assertThat(userService.findUserByEmailAndPassword(userDto).getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userService.findUserByEmailAndPassword(userDto).getName()).isEqualTo(userDto.getName());
        assertThat(userService.findUserByEmailAndPassword(userDto).getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void 등록되지_않은_사용자가_로그인을_하는_경우_예외처리() {
        userDto.setEmail("conie@cony.com");

        assertThrows(NoSuchElementException.class, () -> {
            userService.findUserByEmailAndPassword(userDto);
        });
    }
}