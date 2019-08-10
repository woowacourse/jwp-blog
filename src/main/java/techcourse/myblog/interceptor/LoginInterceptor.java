package techcourse.myblog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("login interceptor request : {}", request.getRequestURI());
        log.debug("login interceptor");
        HttpSession session = request.getSession();
        Object loginSession = session.getAttribute("user");

        if (loginSession == null) {
            log.debug("fail to access request location by interceptor");
            response.sendRedirect("/login");
            return false;
        }
        log.debug("success to access request location");
        return true;
    }
}
