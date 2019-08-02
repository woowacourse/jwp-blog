package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class UserInfoDto implements UserDto {
    private long userId;
    private String name;
    private String email;
    private String snsFacebookEmail;
    private String snsGithubEmail;

    public UserInfoDto() {
    }

    @Builder
    public UserInfoDto(long id, String name, String email, String snsFacebookEmail, String snsGithubEmail) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.snsFacebookEmail = snsFacebookEmail;
        this.snsGithubEmail = snsGithubEmail;
    }

    public static UserDto from(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserDto fromWithSNS(User user, List<SnsInfo> snsInfos) {
        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .snsFacebookEmail(getSnsEmail(snsInfos, 0))
                .snsGithubEmail(getSnsEmail(snsInfos, 1)).build();
    }

    private static String getSnsEmail(List<SnsInfo> snsInfos, int snsCode) {
        return snsInfos.stream()
                .filter(snsInfo -> snsInfo.getSnsCode() == snsCode)
                .findFirst().orElse(SnsInfo.builder().email(null).build())
                .getEmail();
    }

    @Override
    public User toEntity() {
        return User.builder()
                .id(userId)
                .name(name)
                .email(email).build();
    }

    public SnsInfo toSnsInfoByFaceBook(User user) {
        return SnsInfo.builder()
                .snsCode(0)
                .email(snsFacebookEmail)
                .user(user).build();
    }

    public SnsInfo toSnsInfoByGithub(User user) {
        return SnsInfo.builder()
                .snsCode(1)
                .email(snsGithubEmail)
                .user(user).build();
    }

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", snsFacebookEmail='" + snsFacebookEmail + '\'' +
                ", snsGithubEmail='" + snsGithubEmail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto that = (UserInfoDto) o;
        return userId == that.userId &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(snsFacebookEmail, that.snsFacebookEmail) &&
                Objects.equals(snsGithubEmail, that.snsGithubEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, snsFacebookEmail, snsGithubEmail);
    }
}
