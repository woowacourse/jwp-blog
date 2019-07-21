package techcourse.myblog.web;

import org.springframework.ui.Model;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class LoginUtil {
    public static final String SESSION_USER_KEY = "user";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    public static void checkAndPutUser(Model model, Optional<User> currentUser) {
        currentUser.ifPresent(user -> model.addAttribute(SESSION_USER_KEY, user));
    }
}
