package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.exception.SignUpFailException;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void checkConfirmPassword() {
        assertThrows(SignUpFailException.class, () -> {
            UserSignUpRequestDto userSignUpRequestDto = UserSignUpRequestDto.builder().userName("Martin")
                    .email("oeeen3@gmail.com")
                    .password("Aa12345!")
                    .confirmPassword("Aa12345@")
                    .build();
            User user = new User(userSignUpRequestDto);
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
