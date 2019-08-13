package techcourse.myblog.presentation.support;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import techcourse.myblog.domain.user.User;

@Getter
@EqualsAndHashCode
public class LoginUser {
    private User user;

    public LoginUser(User user) {
        this.user = user;
    }
}
