package techcourse.myblog.web.util;

import org.springframework.stereotype.Component;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@Component
public class LoginChecker {
    public static final String LOGGED_IN_USER = "loggedInUser";

    public boolean isLoggedInSameId(HttpSession session, Long id) {
        UserSessionDto loggedInUser = getLoggedInUser(session);
        return loggedInUser.getId().equals(id);
    }

    public boolean isLoggedIn(HttpSession session) {
        try {
            getLoggedInUser(session);
            return true;
        } catch (NotLoggedInException e) {
            return false;
        }
    }

    @NotNull
    public UserSessionDto getLoggedInUser(HttpSession session) {
        UserSessionDto user = (UserSessionDto) session.getAttribute(LOGGED_IN_USER);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
    }
}
