package techcourse.myblog.utils.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.utils.model.ModelLogger;

import javax.servlet.http.HttpSession;

public class SessionHelper {
    private static final Logger log = LoggerFactory.getLogger(ModelLogger.class);
    private static final String BEFORE_SESSION = "Before Session : {} ";
    private static final String AFTER_SESSION = "After Session : {} ";

    public static void setAttribute(HttpSession session, String attribute, Object info) {
        log.info(BEFORE_SESSION, session.getAttribute(attribute));
        session.setAttribute(attribute, info);
        log.info(AFTER_SESSION, session.getAttribute(attribute));
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

    public static boolean isNull(HttpSession session) {
        return session.getAttribute(SessionContext.USER) == null;
    }
}
