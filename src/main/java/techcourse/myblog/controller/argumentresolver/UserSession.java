package techcourse.myblog.controller.argumentresolver;

import lombok.Setter;
import techcourse.myblog.domain.User;

@Setter
public class UserSession {
    private User user;

    public UserSession(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
