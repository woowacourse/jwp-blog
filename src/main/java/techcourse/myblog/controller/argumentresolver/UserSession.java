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
        if (user == null) {
            throw new InvalidUserSessionException("세션 만료");
        }
        return user;
    }
}
