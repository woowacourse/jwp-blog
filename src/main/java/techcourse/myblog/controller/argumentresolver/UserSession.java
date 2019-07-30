package techcourse.myblog.controller.argumentresolver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.InvalidUserSessionException;

@AllArgsConstructor
@Setter
public class UserSession {
    private User user;

    public User getUser() {
        return user;
    }
}
