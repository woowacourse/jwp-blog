package techcourse.myblog.web;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import techcourse.myblog.domain.dto.response.LoginUser;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LogoutInterceptor extends HandlerInterceptorAdapter {
	private static String VALID_URL = "\\/articles\\/[0-9]*";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LoginUser user = (LoginUser) request.getSession().getAttribute("user");
		String path = request.getRequestURI();

		if (Pattern.matches(VALID_URL, path) && request.getMethod().equals("GET")) {
			return true;
		}

		if (user == null) {
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}

}
