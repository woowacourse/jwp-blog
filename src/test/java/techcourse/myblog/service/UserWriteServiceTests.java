package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.common.UserCommonTests;
import techcourse.myblog.service.dto.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserWriteServiceTests extends UserCommonTests {

    private static final Logger log = LoggerFactory.getLogger(UserWriteServiceTests.class);

    @Test
    void 중복_이메일_체크() {
        assertThrows(DuplicateEmailException.class, () -> userWriteService.save(userDto));
    }

    @Test
    void 유저_수정_확인() {
        UserDto updateUserDto = new UserDto("ab", "abcd@abcd", "12345678!Aa");
        userWriteService.update(user, updateUserDto);
        Optional<User> optionalUser = userReadService.findById(user.getId());

        assertEquals(updateUserDto.getName(), optionalUser.get().getName());
    }
}
