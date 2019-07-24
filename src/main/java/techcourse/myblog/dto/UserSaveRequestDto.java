package techcourse.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import techcourse.myblog.domain.User;

@Getter
@AllArgsConstructor
public class UserSaveRequestDto {
    private static final Long EMPTY_ID = -1l;

    private String name;
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .id(EMPTY_ID)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    public User toEntityWithId(Long id) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    @Override
    public String toString() {
        return "UserSaveRequestDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
