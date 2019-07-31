package techcourse.myblog.support.argument;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

@Getter
@Setter
public class LoginUser {
    private User user;
}
