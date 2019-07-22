package techcourse.myblog.users;

import lombok.Getter;

@Getter
public class UserSession {
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
