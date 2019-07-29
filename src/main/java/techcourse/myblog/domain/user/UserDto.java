package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private String snsFacebookEmail;
    private String snsGithubEmail;

    public UserDto() {
    }

    @Builder
    public UserDto(long id, String name, String email, String password, String snsFacebookEmail, String snsGithubEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.snsFacebookEmail = snsFacebookEmail;
        this.snsGithubEmail = snsGithubEmail;
    }

    public static UserDto from(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .snsFacebookEmail(user.getSnsEmailBySnsCode(0))
                .snsGithubEmail(user.getSnsEmailBySnsCode(1))
                .build();
    }

    public static UserDto fromWithoutPassword(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .snsFacebookEmail(user.getSnsEmailBySnsCode(0))
                .snsGithubEmail(user.getSnsEmailBySnsCode(1))
                .build();
    }

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();

        if (snsFacebookEmail != null && snsFacebookEmail.length() > 0) {
            user.addSns(snsFacebookEmail, 0L);
        }
        if (snsGithubEmail != null && snsFacebookEmail.length() > 0) {
            user.addSns(snsGithubEmail, 1L);
        }

        return user;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", snsFacebookEmail='" + snsFacebookEmail + '\'' +
                ", snsGithubEmail='" + snsGithubEmail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(password, userDto.password) &&
                Objects.equals(snsFacebookEmail, userDto.snsFacebookEmail) &&
                Objects.equals(snsGithubEmail, userDto.snsGithubEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, snsFacebookEmail, snsGithubEmail);
    }
}
