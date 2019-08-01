package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.common.UserCommonServiceTests;
import techcourse.myblog.web.controller.LoginFailedException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class UserReadServiceTests extends UserCommonServiceTests {
    @Test
    public void findByEmailAndPassword_test() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userReadService.login(new UserDto("", user.getEmail(), "Passw0rd!")));
        assertThrows(LoginFailedException.class, () ->
                userReadService.login(new UserDto("", "e@mail.com", "Passw0rd!")));
    }

    @Test
    public void findAll_test() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        userReadService.findAll().forEach(foundUser ->
            compareUser(user, foundUser));
    }
}