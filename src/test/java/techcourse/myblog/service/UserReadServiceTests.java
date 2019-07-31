package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.service.common.UserCommonServiceTests;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.exception.LoginFailedException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class UserReadServiceTests extends UserCommonServiceTests {
    @Test
    void 이메일_패스워드_일치() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .willReturn(Optional.of(user));

        assertEquals(userReadService.findByEmailAndPassword(new UserDto("", user.getEmail(), user.getPassword())), user);
    }
    
    @Test
    void 이메일_패스워드_일치_실패() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .willReturn(Optional.of(user));
        
        assertThrows(LoginFailedException.class, () -> {
            userReadService.findByEmailAndPassword(new UserDto("", "e@mail.com", "Passw0rd!"));
        });
    }
    
    @Test
    void 유저_리스트_성공() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        userReadService.findAll().forEach(foundUser ->
            compareUser(user, foundUser));
    }
}