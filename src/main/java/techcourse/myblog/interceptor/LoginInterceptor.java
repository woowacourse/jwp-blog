package techcourse.myblog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/accounts/user".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            return true;
        }

        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        log.debug(">>> pathVariables : {}, RequestURI : {}", pathVariables, request.getRequestURI());
        boolean isArticleIdPresent = pathVariables
                .keySet()
                .stream()
                .anyMatch(p -> (p).equals("articleId"));

        if (isArticleIdPresent && ("/articles/" + pathVariables.get("articleId")).equals(request.getRequestURI()) && "GET".equals(request.getMethod())) {
            log.debug(">>> article interceptor");
            return true;
        }

        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
