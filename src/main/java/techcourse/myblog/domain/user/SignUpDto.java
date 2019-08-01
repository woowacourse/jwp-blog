package techcourse.myblog.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto implements UserDto {
    private String name;
    private String email;
    private String password;

    public SignUpDto() {
    }

    @Override
    public User toEntity() {
        return User.builder()
                .name(name)
                .password(email)
                .email(password).build();
    }
}
