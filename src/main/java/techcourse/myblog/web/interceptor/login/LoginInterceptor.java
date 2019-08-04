package techcourse.myblog.web.interceptor.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Pattern PATTERN = Pattern.compile("^\\/articles\\/[\\d]+$");
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        //log.info("uri : {} ", pathVariables);

        Matcher matcher = PATTERN.matcher(request.getRequestURI());
        if (matcher.find() && request.getMethod().equals("GET")) {
            log.info("uri : {} ", request.getRequestURI());
            return true;
        }

        HttpSession session = request.getSession();
        if (SessionUtil.isNull(session)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}