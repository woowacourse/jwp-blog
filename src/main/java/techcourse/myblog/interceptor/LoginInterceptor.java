package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.web.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getSession().getAttribute(UserInfo.NAME) == null) {
            return sendRedirect(response);
        }
        return true;
    }

    private boolean sendRedirect(HttpServletResponse response) throws IOException {
        try {
            response.sendRedirect("/login");
            return false;
        } catch (IOException e) {
            throw new IOException();
        }
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e) {
        log.info(e.getMessage());
    }
}