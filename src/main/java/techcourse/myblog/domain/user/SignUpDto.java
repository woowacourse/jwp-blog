package techcourse.myblog.domain.user;

import lombok.Builder;
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

    @Builder
    public SignUpDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public User toEntity() {
        return User.builder()
                .name(name)
                .password(password)
                .email(email).build();
    }

    @Override
    public String toString() {
        return "SignUpDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
