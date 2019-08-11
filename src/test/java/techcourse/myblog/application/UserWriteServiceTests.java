package techcourse.myblog.application;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.common.UserCommonServiceTests;
import techcourse.myblog.application.exception.DuplicatedEmailException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.utils.UserTestObjects.*;

public class UserWriteServiceTests extends UserCommonServiceTests {
    @Test
    public void modify_test() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        userWriteService.update(user, UPDATE_USER_DTO);
        compareUser(UserAssembler.toEntity(UPDATE_USER_DTO), UserAssembler.buildUserResponseDto(userRepository.findByEmail(user.getEmail()).get()));
    }
}
