package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    void 이메일_등록() {
        userDto = new UserDto("ab", "abcd@abcd", "12345678!Aa");
        userService.save(userDto);
    }

    @Test
    void 중복_이메일_체크() {
        assertThrows(DuplicateEmailException.class, () -> userService.save(userDto));
    }
}
