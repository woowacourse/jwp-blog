package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequest;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.FailedPasswordVerificationException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void 중복된_이메일로_가입하는_경우_예외를_던진다() {
        UserRequest userDto =
                new UserRequest("cony", "cony@cony.com", "@Password12", "@Password12");

        assertThrows(DuplicateEmailException.class,
                () -> userService.save(userDto));
    }

    @Test
    public void 비밀번호_확인을_비밀번호와_다르게_입력하는_경우_예외를_던진다() {
        UserRequest userDto =
                new UserRequest("tony", "tony@tony.com", "@Password12", "123");

        assertThrows(FailedPasswordVerificationException.class,
                () -> userService.save(userDto));
    }

    @Test
    public void 이메일과_비밀번호로_사용자를_잘_찾는지_확인한다() {
        UserRequest userDto =
                new UserRequest("sony", "sony@sony.com", "@Password12", "@Password12");
        userService.save(userDto);

        User user = userService.findUserByEmailAndPassword(userDto);

        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void 등록되지_않은_이메일로_로그인하는_경우_예외를_던진다() {
        UserRequest userDto =
                new UserRequest("conie", "conie@conie.com", "@Password12", "@Password12");

        assertThrows(FailedLoginException.class, () -> {
            userService.findUserByEmailAndPassword(userDto);
        });
    }

    @Test
    public void 로그인_비밀번호를_틀린_경우_예외를_던진다() {
        UserRequest userDto =
                new UserRequest("cony", "cony@cony.com", "@Pass", "@Pass");

        assertThrows(FailedLoginException.class, () -> {
            userService.findUserByEmailAndPassword(userDto);
        });
    }

    @Test
    public void 회원정보_수정을_잘_하는지_확인한다() {
        UserRequest userDto =
                new UserRequest("conie", "cony@cony.com", "@Password12", "@Password12");
        User updatedUser = userService.update(userDto);

        assertThat(updatedUser.getName()).isEqualTo(userDto.getName());
    }

    @Test
    public void 탈퇴가_잘_되는지_확인한다() {
        userService.deleteByEmail("buddy@buddy.com");

        assertThrows(NoSuchElementException.class, () -> {
            userService.findUserByEmail("buddy@buddy.com");
        });
    }
}