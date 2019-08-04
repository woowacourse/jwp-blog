package techcourse.myblog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private static final String USER_NAME = "test";
    private static final String UPDATE_USER_NAME = "updateTest";

    private static final String EMAIL = "test@test.com";

    private static final String PASSWORD = "passWord!1";
    private static final String UPDATE_PASSWORD = "updatePassWord!1";

    private static final String WRONG_PASSWORD = "passWord!12";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("사용자 정보를 변경한다.")
    void updateUser() {
        User updatedUser = new User(UPDATE_USER_NAME, EMAIL, UPDATE_PASSWORD);

        user.update(updatedUser);
        assertThat(user.getUserName()).isEqualTo(updatedUser.getUserName());
        assertThat(user.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(updatedUser.getPassword());
    }

    @Test
    @DisplayName("비밀번호가 맞는지 체크한다.")
    void checkPassword() {
        assertThat(user.checkPassword(PASSWORD)).isTrue();
        assertThat(user.checkPassword(WRONG_PASSWORD)).isFalse();
    }
}