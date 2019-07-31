package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.common.UserCommonServiceTests;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

public class UserReadServiceTests extends UserCommonServiceTests {
    @Test
    public void findByEmailAndPassword_test() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .willReturn(Optional.of(user));

        assertTrue(userReadService.findByEmailAndPassword(new UserDto("", user.getEmail(), "Passw0rd!")).isPresent());
        assertFalse(userReadService.findByEmailAndPassword(new UserDto("", "e@mail.com", "Passw0rd!")).isPresent());
    }

    @Test
    public void findAll_test() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        userReadService.findAll().forEach(foundUser ->
            compareUser(user, foundUser));
    }
}