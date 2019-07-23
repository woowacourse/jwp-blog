package techcourse.myblog.users;

import lombok.Getter;

@Getter
public class UserSession {
    public static final String USER_SESSION = "user";

    private Long id;
    private String email;
    private String name;

    private UserSession(User user) {
        id = user.getId();
        email = user.getEmail();
        name = user.getName();
    }

    public static UserSession createByUser(User user) {
        return new UserSession(user);
    }
}
