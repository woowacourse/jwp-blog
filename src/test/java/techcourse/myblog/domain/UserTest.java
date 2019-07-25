package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("kimhyojae");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다.")
    void updateUserTest() {
        // Given
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("update");

        // When
        user.updateUser(userUpdateDto.getName());

        // Then
        assertThat(user.getName()).isEqualTo("update");
    }
}