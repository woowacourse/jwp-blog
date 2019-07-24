package techcourse.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMypageRequestDto {
    private static final Long EMPTY_ID = -1l;

    private final Long id;
    private final String name;
    private final String email;
    private final String snsGithubEmail;
    private final String snsFacebookEmail;

    @Override
    public String toString() {
        return "UserMypageRequestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", snsGithubEmail='" + snsGithubEmail + '\'' +
                ", snsFacebookEmail='" + snsFacebookEmail + '\'' +
                '}';
    }
}
