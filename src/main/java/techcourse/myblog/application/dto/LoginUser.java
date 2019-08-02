package techcourse.myblog.application.dto;
import techcourse.myblog.domain.User;

public class LoginUser {
    private User user;

    public LoginUser(final User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}