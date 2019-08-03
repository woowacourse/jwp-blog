package techcourse.myblog.web.support;

import techcourse.myblog.domain.User;

public class SessionUser {
    private User user;

    public SessionUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean matchId(Long userId) {
        return user.matchId(userId);
    }
}
