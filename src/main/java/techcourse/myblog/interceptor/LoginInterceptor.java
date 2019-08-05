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
        if (isRegistAccountRequest(request)) {
            return true;
        }

        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        boolean isArticleIdPresent = isArticleIdPresent(pathVariables);

        if (isGetArticleRequest(request, pathVariables, isArticleIdPresent)) {
            return true;
        }

        if (isNotLogin(request)) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private boolean isArticleIdPresent(Map<?, ?> pathVariables) {
        return pathVariables
                .keySet()
                .stream()
                .anyMatch(p -> (p).equals("articleId"));
    }

    private boolean isNotLogin(HttpServletRequest request) {
        return request.getSession().getAttribute("user") == null;
    }

    private boolean isGetArticleRequest(HttpServletRequest request, Map<?, ?> pathVariables, boolean isArticleIdPresent) {
        return isArticleIdPresent && ("/articles/" + pathVariables.get("articleId")).equals(request.getRequestURI()) && "GET".equals(request.getMethod());
    }

    private boolean isRegistAccountRequest(HttpServletRequest request) {
        return "/accounts/user".equals(request.getRequestURI()) && "POST".equals(request.getMethod());
    }
}
