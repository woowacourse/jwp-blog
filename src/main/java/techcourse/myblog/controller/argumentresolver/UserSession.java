package techcourse.myblog.controller.argumentresolver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

@AllArgsConstructor
@Getter
@Setter
public class UserSession {
    private User user;
}
