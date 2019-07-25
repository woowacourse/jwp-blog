package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.common.UserCommonServiceTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class UserWriteServiceTests extends UserCommonServiceTests {
    @Test
    public void save_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userWriteService.save(new UserDto("name", "e@mail.com", "Passw0rd!")));
        assertThrows(DuplicatedEmailException.class, () ->
                userWriteService.save(new UserDto("name", user.getEmail(), "Passw0rd!")));
    }

    @Test
    public void modify_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        UserDto userDto = new UserDto("nameee", user.getEmail(), user.getPassword());
        userWriteService.modify(user, userDto);
        compareUser(userDto.toUser(), userRepository.findByEmail(user.getEmail()).get());
    }
}
