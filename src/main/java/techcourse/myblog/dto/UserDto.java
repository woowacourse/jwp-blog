package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDto {
    @Pattern(regexp = "^([a-zA-Z]){2,10}$")
    private String name;
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$")
    private String password;

    public User toEntity() {
        return new User(name, email, password);
    }
}
