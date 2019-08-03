package techcourse.myblog.support.argument;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

@Getter
public class LoginUser {
    private User user;

    public LoginUser(User user) {
        this.user = user;
    }
}
