package techcourse.myblog.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {
    @Test
    void javaBean() {
        UserDto userDto = new UserDto();
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";

        userDto.setEmail(email);
        userDto.setName(name);
        userDto.setPassword(password);
        userDto.setConfirmPassword(password);

        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getName()).isEqualTo(name);
        assertThat(userDto.getPassword()).isEqualTo(password);
        assertThat(userDto.getConfirmPassword()).isEqualTo(password);
    }
}