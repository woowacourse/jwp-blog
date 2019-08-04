package techcourse.myblog.presentation.argument;

import lombok.Getter;
import techcourse.myblog.domain.user.User;

@Getter
public class LoginUser {
    private User user;

    public LoginUser(User user) {
        this.user = user;
    }
}
