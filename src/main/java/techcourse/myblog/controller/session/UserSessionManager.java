package techcourse.myblog.controller.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import techcourse.myblog.domain.User;

@Component
public class UserSessionManager extends SessionManager {

    public UserSessionManager(HttpServletRequest request) {
        super(request);
    }

    public void setUser(User user) {
        super.set("user", user);
    }

    public User getUser() {
        return (User) super.get("user");
    }

    public void removeUser() {
        super.remove("user");
    }
}
