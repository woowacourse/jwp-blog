package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.common.UserCommonServiceTests;
import techcourse.myblog.service.exception.LoginFailedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserReadServiceTests extends UserCommonServiceTests {
    @Test
    public void login_success_test() {
        assertDoesNotThrow(() -> userReadService.login(new UserDto("", user.getEmail(), user.getPassword())));
    }

    @Test
    public void login_fail_test() {
        UserDto notJoinedUserDto = new UserDto("", "not@mail.com", "Passw0rd!");
        assertThrows(LoginFailedException.class, () ->
                userReadService.login(notJoinedUserDto));
    }
}