package techcourse.myblog.support.argument.resolver;

import lombok.Data;
import techcourse.myblog.domain.User;

@Data
public class SessionInfo {
    private User user;
}
