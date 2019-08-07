package techcourse.myblog.web.argumentResolver;

import techcourse.myblog.user.dto.UserResponse;

public class AccessUserInfo {
    private final UserResponse user;

    public AccessUserInfo(final UserResponse user) {
        this.user = user;
    }

    public boolean match(UserResponse user) {
        return this.user.equals(user);
    }

    public UserResponse getUser() {
        return user;
    }
}
