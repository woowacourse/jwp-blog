package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.FailedPasswordVerificationException;

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
    public void 중복된_이메일로_가입하는_경우_예외를_던진다() {
        userDto.setEmail("cony@cony.com");
        userService.save(userDto);

        assertThrows(DuplicateEmailException.class,
                () -> userService.save(userDto));
    }

    @Test
    public void 비밀번호_확인을_비밀번호와_다르게_입력하는_경우_예외를_던진다() {
        userDto.setEmail("bony@cony.com");
        userDto.setPasswordConfirm("123");

        assertThrows(FailedPasswordVerificationException.class,
                () -> userService.save(userDto));
    }

    @Test
    public void 등록된_사용자의_로그인이_잘_되는지_확인한다() {
        userDto.setEmail("sony@cony.com");
        userService.save(userDto);

        assertThat(userService.findUserByEmailAndPassword(userDto).getName()).isEqualTo(userDto.getName());
        assertThat(userService.findUserByEmailAndPassword(userDto).getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userService.findUserByEmailAndPassword(userDto).getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void 등록되지_않은_이메일로_로그인하는_경우_예외를_던진다() {
        userDto.setEmail("conie@cony.com");

        assertThrows(FailedLoginException.class, () -> {
            userService.findUserByEmailAndPassword(userDto);
        });
    }

    @Test
    public void 로그인_비밀번호를_틀린_경우_예외를_던진다() {
        userDto.setEmail("dony@cony.com");
        userService.save(userDto);
        userDto.setPassword("@pAssword123");

        assertThrows(FailedLoginException.class, () -> {
            userService.findUserByEmailAndPassword(userDto);
        });
    }
}