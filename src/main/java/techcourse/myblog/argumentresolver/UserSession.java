package techcourse.myblog.argumentresolver;

import lombok.Getter;

@Getter
public class UserSession {
    private long id;
    private String email;
    private String name;

    public UserSession(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
