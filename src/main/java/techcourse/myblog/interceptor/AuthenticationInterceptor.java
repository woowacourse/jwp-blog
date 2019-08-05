package techcourse.myblog.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws IOException {
		Object email = request.getSession().getAttribute("email");
		String path = request.getRequestURI();
		if (email != null) {
			return isAccessibleLoginUser(response, path);
		}
		return isAccessibleNonLoginUser(response, path);
	}

	private boolean isAccessibleNonLoginUser(HttpServletResponse response, String path) throws IOException {
		if((!"/".equals(path)) && (!"/signup".equals(path)) && (!"/login".equals(path)) && (!"/users".equals(path))) {
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}

	private boolean isAccessibleLoginUser(HttpServletResponse response, String path) throws IOException {
		if("/login".equals(path) || "/signup".equals(path)) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
}
