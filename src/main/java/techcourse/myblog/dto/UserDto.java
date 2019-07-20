package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private String password;

    public User toEntity() {
        return new User(name, email, password);
    }
}
