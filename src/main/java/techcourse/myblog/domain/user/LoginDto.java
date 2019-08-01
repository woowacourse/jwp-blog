package techcourse.myblog.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto implements UserDto {
    private String email;
    private String password;

    public LoginDto() {
    }

    @Override
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
