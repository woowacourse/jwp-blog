package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import techcourse.myblog.domain.User;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserSaveRequestDto {
    private String name;
    private String email;
    private String password;

    public UserSaveRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
