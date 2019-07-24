package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.User;

import java.util.Objects;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String snsGithubEmail;
    private final String snsFacebookEmail;

    public UserResponseDto(Long id, String name, String email, String snsGithubEmail, String snsFacebookEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.snsGithubEmail = snsGithubEmail;
        this.snsFacebookEmail = snsFacebookEmail;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId()
                , user.getName()
                , user.getEmail()
                , user.getSnsInfo(0).map(snsInfo -> snsInfo.getEmail()).orElse("")
                , user.getSnsInfo(1).map(snsInfo -> snsInfo.getEmail()).orElse(""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDto that = (UserResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(snsGithubEmail, that.snsGithubEmail) &&
                Objects.equals(snsFacebookEmail, that.snsFacebookEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, snsGithubEmail, snsFacebookEmail);
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", snsGithubEmail='" + snsGithubEmail + '\'' +
                ", snsFacebookEmail='" + snsFacebookEmail + '\'' +
                '}';
    }
}
