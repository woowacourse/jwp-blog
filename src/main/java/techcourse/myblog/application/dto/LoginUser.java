package techcourse.myblog.application.dto;
import techcourse.myblog.domain.User;

import java.lang.annotation.*;

public class LoginUser {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}