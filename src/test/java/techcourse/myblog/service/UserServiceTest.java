package techcourse.myblog.service;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void 유저_등록() {
        userDto = new UserDto("ab", "abcd@abcd", "12345678!Aa");
        user = userService.save(userDto);
    }

    @Test
    void 중복_이메일_체크() {
        assertThrows(DuplicateEmailException.class, () -> userService.save(userDto));
    }

    @Test
    void 유저_전체_조회() {
        assertDoesNotThrow(() -> userService.findAll());
    }

    @Test
    void 유저_이메일_및_비밀번호_일치_확인() {
        Optional<User> userOptional = userService.findByEmailAndPassword(userDto);
        assertTrue(userOptional.isPresent());
    }

    @Test
    void 유저_수정_확인() {
        UserDto updateUserDto = new UserDto("abcd", null, null);
        Optional<User> userOptional = userService.update(user.getId(), updateUserDto);
        assertEquals("abcd", userOptional.get().getName());
    }

    @AfterEach()
    void 유저_삭제() {
        userService.deleteById(user.getId());
    }
}
