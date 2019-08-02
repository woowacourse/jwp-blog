package techcourse.myblog.controller.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public abstract class AbstractSessionManager implements SessionManager {
    private final HttpServletRequest request;

    @Autowired
    public AbstractSessionManager(HttpServletRequest request) {
        this.request = request;
    }

    public void set(String sessionKey, Object sessionValue) {
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, sessionValue);
    }

    public Object get(String sessionKey) {
        HttpSession session = request.getSession();
        return session.getAttribute(sessionKey);
    }

    public void remove(String sessionKey) {
        HttpSession session = request.getSession();
        session.removeAttribute(sessionKey);
    }
}
