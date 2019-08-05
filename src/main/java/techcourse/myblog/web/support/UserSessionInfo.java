package techcourse.myblog.web.support;

import techcourse.myblog.domain.user.User;

public class UserSessionInfo {

    private final User user;


    public UserSessionInfo(User user) {
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public User toUser() {
        return user;
    }
}
