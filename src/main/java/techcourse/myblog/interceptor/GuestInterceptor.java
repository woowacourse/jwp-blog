package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

import static techcourse.myblog.users.UserSession.USER_SESSION;

@Component
public class GuestInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(GuestInterceptor.class);

    private static final Pattern ARTICLE_PATTERN = Pattern.compile("^/articles/[0-9]*$");

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("PATH : {} METHOD : {} ", path, method);

        if (session.getAttribute(USER_SESSION) == null && !isException(path, method)) {
            response.sendRedirect("/users/login");
            return false;
        }
        return true;
    }

    private boolean isException(final String path, final String method) {
        return isSignup(path, method) || isShow(path, method);
    }


    private boolean isSignup(String path, String method) {
        return path.equals("/users") && method.equals("POST");
    }

    private boolean isShow(String path, String method){
        return ARTICLE_PATTERN.matcher(path).find() && method.equals("GET");
    }

}