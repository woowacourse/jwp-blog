package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void checkConfirmPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserDto userDto = UserDto.builder().userName("Martin")
                    .email("oeeen3@gmail.com")
                    .password("Aa12345!")
                    .confirmPassword("Aa12345@")
                    .build();
            User user = new User(userDto);
        });
    }

    @Test
    void matchPassword() {
        User user = User.builder()
                .userName("Martin")
                .email("oeeen3@gmail.com")
                .password("Aa12345!")
                .build();
        assertTrue(user.matchPassword("Aa12345!"));
        assertFalse(user.matchPassword("Aa12345@"));
    }
}
