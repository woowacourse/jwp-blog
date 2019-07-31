package techcourse.myblog.web.support;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class LoginSessionManager {

    private final HttpServletRequest httpServletRequest;

    public LoginSessionManager(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setLoginSession(User user) {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", user);
    }

    public void clearSession() {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("user");
    }

}
