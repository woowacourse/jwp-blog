package techcourse.myblog.controller.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
class AbstractSessionManager {
    private final HttpServletRequest request;

    @Autowired
    protected AbstractSessionManager(HttpServletRequest request) {
        this.request = request;
    }

    void set(String sessionKey, Object sessionValue) {
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, sessionValue);
    }

    Object get(String sessionKey) {
        HttpSession session = request.getSession();
        return session.getAttribute(sessionKey);
    }

    void remove(String sessionKey) {
        HttpSession session = request.getSession();
        session.removeAttribute(sessionKey);
    }
}
