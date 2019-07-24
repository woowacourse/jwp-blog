package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.common.UserCommonTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserReadServiceTests extends UserCommonTests {
    @Test
    void 유저_전체_조회() {
        assertDoesNotThrow(() -> userReadService.findAll());
    }
    
    @Test
    void 유저_이메일_및_비밀번호_일치_확인() {
        Optional<User> userOptional = userReadService.findByEmailAndPassword(userDto);
        assertTrue(userOptional.isPresent());
    }
}
