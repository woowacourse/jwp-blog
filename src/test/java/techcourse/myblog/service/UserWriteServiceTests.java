package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.common.UserCommonTests;
import techcourse.myblog.service.dto.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserWriteServiceTests extends UserCommonTests {
    @Test
    void 중복_이메일_체크() {
        assertThrows(DuplicateEmailException.class, () -> userWriteService.save(userDto));
    }

    @Test
    void 유저_수정_확인() {
        UserDto updateUserDto = new UserDto("abcd", null, null);
        Optional<User> userOptional = userWriteService.update(user.getId(), updateUserDto);
        assertEquals("abcd", userOptional.get().getName());
    }
}
