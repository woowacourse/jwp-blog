package techcourse.myblog.web;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public class ControllerUtil {
    public static final String SESSION_USER_KEY = "user";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    public static void checkAndPutUser(Model model, HttpSession session) {
        if (isLoggedIn(session)) {
            model.addAttribute(SESSION_USER_KEY, session.getAttribute(SESSION_USER_KEY));
        }
    }
}
