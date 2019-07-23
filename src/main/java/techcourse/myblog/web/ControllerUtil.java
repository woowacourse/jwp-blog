package techcourse.myblog.web;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public class ControllerUtil {
    private static final String LOGIN_SESSION_KEY = "loginUser";

    public static void putLoginUser(HttpSession session, Model model) {
        if (session.getAttribute(LOGIN_SESSION_KEY) != null) {
            model.addAttribute(LOGIN_SESSION_KEY, session.getAttribute(LOGIN_SESSION_KEY));
        }
    }
}
