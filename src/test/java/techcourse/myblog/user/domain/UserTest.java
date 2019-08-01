package techcourse.myblog.user.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.data.UserDataForTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private final User user = User.builder()
            .id(1)
            .email(UserDataForTest.USER_EMAIL)
            .password(UserDataForTest.USER_PASSWORD)
            .name(UserDataForTest.USER_NAME)
            .build();

    @Test
    void 업데이트() {
        user.update("NewName");
        assertThat(user.getName().getName()).isEqualTo("NewName");
    }

    @Test
    void checkPassword() {
        assertTrue(user.checkPassword(UserDataForTest.USER_PASSWORD));
    }
}