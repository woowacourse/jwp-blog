package techcourse.myblog.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	public AuthenticationInterceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws IOException {
		String email = (String) request.getSession().getAttribute("email");
		String path = request.getRequestURI();

		if (email != null) {
			if (path.equals("/login") || path.equals("/signup")) {
				response.sendRedirect("/");
				return false;
			}
			return true;
		} else {
			if (!(path.equals("/mypage") || path.equals("/mypage/edit") || path.equals("/leave"))) {
				response.sendRedirect("/login");
				return false;
			}
			return true;
		}
	}
}
