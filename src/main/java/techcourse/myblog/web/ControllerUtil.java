package techcourse.myblog.web;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public class ControllerUtil {
    private static final String LOGIN_SESSION_KEY = "loginUser";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loginUser") != null;
    }

    public static void putLoginUser(HttpSession session, Model model) {
        if (isLoggedIn(session)) {
            model.addAttribute(LOGIN_SESSION_KEY, session.getAttribute(LOGIN_SESSION_KEY));
        }
    }
}
