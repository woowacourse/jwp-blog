package techcourse.myblog.web.argumentResolver;

import techcourse.myblog.service.dto.user.UserResponse;

public class AccessUserInfo {
    private final UserResponse user;

    public AccessUserInfo(final UserResponse user) {
        this.user = user;
    }

    public UserResponse getUser() {
        return user;
    }
}
