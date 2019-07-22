package techcourse.myblog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static final Logger log = LoggerFactory.getLogger(ModelUtil.class);
    private static final String BEFORE_SESSION = "Before Session : {} ";
    private static final String AFTER_SESSION = "After Session : {} ";

    public static void setAttribute(HttpSession session, String attribute, String info) {
        log.info(BEFORE_SESSION, session.getAttribute(info));
        session.setAttribute(attribute, info);
        log.info(AFTER_SESSION, session.getAttribute(info));
    }

    public static void removeAttribute(HttpSession session, String attribute) {
        log.info(BEFORE_SESSION, session);
        session.removeAttribute(attribute);
        log.info(AFTER_SESSION, session);
    }

    public static Object getAttribute(HttpSession session, String attribute) {
        Object sessionInfo = session.getAttribute(attribute);
        log.info("Session : {} ", sessionInfo);
        return sessionInfo;
    }
}
