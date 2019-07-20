package techcourse.myblog.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationDto {
    private String email;
    private String password;

    public AuthenticationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
