package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class UserVO {
    private String name;
    private String password;
    private String email;

    @Builder
    public UserVO(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
