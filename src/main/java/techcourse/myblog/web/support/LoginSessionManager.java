package techcourse.myblog.web.support;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class LoginSessionManager {

    private final HttpServletRequest httpServletRequest;

    public LoginSessionManager(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setLoginSession(String name, String email) {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("userName", name);
        session.setAttribute("userEmail", email);
    }

    public void clearSession() {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
    }

}
