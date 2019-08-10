package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.common.UserCommonServiceTests;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpFailedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserWriteServiceTests extends UserCommonServiceTests {
    UserDto updateUserDto = new UserDto("updateName", user.getEmail(), user.getPassword());

    @Test
    public void save_success_test() {
        User notJoinedUser = new User("notJoined", "notJoined@mail.com", "Passw0rd!");
        assertDoesNotThrow(() -> userWriteService.save(notJoinedUser));
    }

    @Test
    public void save_fail_test() {
        User dupEmailUser = new User("dupEmail", user.getEmail(), "Passw0rd!");
        assertThrows(SignUpFailedException.class, () -> userWriteService.save(dupEmailUser));
    }

    @Test
    public void modify_sucess_test() {
        assertDoesNotThrow(() -> userWriteService.update(user, updateUserDto));
        compareUser(updateUserDto.toUser(), userRepository.findByEmail(user.getEmail()).get());
    }

    @Test
    public void modify_fail_test() {
        User other = new User("other", "other@mail.com", "Passw0rd!");

        assertThrows(NotFoundUserException.class, () ->
                userWriteService.update(other, updateUserDto));
    }
}
