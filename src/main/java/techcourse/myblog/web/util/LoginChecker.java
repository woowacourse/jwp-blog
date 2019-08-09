package techcourse.myblog.web.util;

import org.springframework.stereotype.Component;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class LoginChecker {
    public static final String LOGGED_IN_USER = "loggedInUser";

    public boolean isLoggedInSameId(HttpSession session, Long id) {
        UserSessionDto loggedInUser = getLoggedInUser(session).orElseThrow(NotLoggedInException::new);
        return loggedInUser.getId().equals(id);
    }

    public boolean isLoggedIn(HttpSession session) {
        return getLoggedInUser(session).isPresent();
    }

    private Optional<UserSessionDto> getLoggedInUser(HttpSession session) {
        return Optional.ofNullable((UserSessionDto) session.getAttribute(LOGGED_IN_USER));
    }
}
