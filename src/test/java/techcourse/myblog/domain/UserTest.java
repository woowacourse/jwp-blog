package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.userinfo.UserPassword;
import techcourse.myblog.dto.UserEditRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void matchPassword() {
        User user = User.builder()
                .name("name")
                .email("test@test.com")
                .password("password1!")
                .build();

        UserPassword rightUserPassword = new UserPassword("password1!");
        UserPassword wrongUserPassword = new UserPassword("password2@");

        assertThat(user.matchPassword(rightUserPassword)).isTrue();
        assertThat(user.matchPassword(wrongUserPassword)).isFalse();
    }

    @Test
    void update() {
        User user = User.builder()
                .name("name")
                .email("test@test.com")
                .password("password1!")
                .build();

        UserEditRequestDto userEditRequestDto = new UserEditRequestDto();
        userEditRequestDto.setName("newName");

        user.update(userEditRequestDto);

        assertThat(user.matchName("newName")).isTrue();
    }
}