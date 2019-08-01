package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    public UserInfoDto(long userId, String name, String email, String snsFacebookEmail, String snsGithubEmail) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.snsFacebookEmail = snsFacebookEmail;
        this.snsGithubEmail = snsGithubEmail;
    }

    public static UserDto from(User user) {
        return UserInfoDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserDto fromWithSNS(User user, List<SnsInfo> snsInfos) {
        return UserInfoDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .snsFacebookEmail(getSnsEmail(snsInfos, 0))
                .snsGithubEmail(getSnsEmail(snsInfos, 1)).build();
    }

    private static String getSnsEmail(List<SnsInfo> snsInfos, int snsCode) {
        return snsInfos.stream()
                .filter(snsInfo -> snsInfo.getSnsCode() == snsCode)
                .findFirst().orElse(SnsInfo.builder().email("").build())
                .getEmail();
    }

    @Override
    public User toEntity() {
        return User.builder()
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
}
