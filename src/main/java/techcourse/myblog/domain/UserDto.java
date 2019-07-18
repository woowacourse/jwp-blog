package techcourse.myblog.domain;

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
                .snsFacebookEmail(user.getSnsInfo(0) == null ? null : user.getSnsInfo(0).getEmail())
                .snsGithubEmail(user.getSnsInfo(1) == null ? null : user.getSnsInfo(1).getEmail())
                .build();
    }

    public static UserDto fromWithoutPassword(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .snsFacebookEmail(user.getSnsInfo(0) == null ? null : user.getSnsInfo(0).getEmail())
                .snsGithubEmail(user.getSnsInfo(1) == null ? null : user.getSnsInfo(1).getEmail())
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
            user.addSnsInfo(SnsInfo.builder().email(snsFacebookEmail).user(user).build());
        }
        if (snsGithubEmail != null) {
            user.addSnsInfo(SnsInfo.builder().email(snsGithubEmail).user(user).build());
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

    public void updateUserInfo(UserDto userDto) {
        name = userDto.getName();
        snsFacebookEmail = userDto.snsFacebookEmail;
        snsGithubEmail = userDto.snsGithubEmail;
    }
}
