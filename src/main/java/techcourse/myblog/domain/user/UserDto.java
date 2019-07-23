package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

        if (snsFacebookEmail != null) {
            user.addSns(snsFacebookEmail, 0L);
        }
        if (snsGithubEmail != null) {
            user.addSns(snsGithubEmail, 1L);
        }

        return user;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
