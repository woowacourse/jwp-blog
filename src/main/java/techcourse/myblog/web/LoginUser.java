package techcourse.myblog.web;

import techcourse.myblog.domain.User;

public class LoginUser {
    private User user;

    public LoginUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }
}
