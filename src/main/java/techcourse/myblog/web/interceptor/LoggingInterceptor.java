package techcourse.myblog.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

    private Logger logger;

    public LoggingInterceptor() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("{}: {}", request.getMethod(), request.getRequestURI());
        logger.info("SID: {}", request.getSession().getId());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
        logger.info("STATUS {}", response.getStatus());
    }
}
