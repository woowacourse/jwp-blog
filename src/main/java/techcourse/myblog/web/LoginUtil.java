package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

public class LoginUtil {
    public static final String SESSION_USER_KEY = "user";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }
}
