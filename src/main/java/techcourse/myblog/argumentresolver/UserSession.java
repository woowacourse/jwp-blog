package techcourse.myblog.argumentresolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSession {
    private long id;
    private String email;
    private String name;
}
