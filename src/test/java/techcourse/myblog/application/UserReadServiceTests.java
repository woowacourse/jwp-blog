package techcourse.myblog.application;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.common.UserCommonServiceTests;
import techcourse.myblog.application.exception.LoginFailedException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.utils.UserTestObjects.LOGIN_FAIL_USER_DTO;
import static techcourse.myblog.utils.UserTestObjects.LOGIN_USER_DTO;

public class UserReadServiceTests extends UserCommonServiceTests {
    @Test
    void 이메일_패스워드_일치() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).willReturn(Optional.of(user));

        assertEquals(userReadService.findByEmailAndPassword(LOGIN_USER_DTO), user);
    }
    
    @Test
    void 이메일_패스워드_일치_실패() {
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).willReturn(Optional.of(user));

        assertThrows(LoginFailedException.class, () -> {
            userReadService.findByEmailAndPassword(LOGIN_FAIL_USER_DTO);
        });
    }
    
    @Test
    void 유저_리스트_성공() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        userReadService.findAll().forEach(foundUser -> compareUser(user, foundUser));
    }
}