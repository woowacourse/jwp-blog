package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import techcourse.myblog.domain.dto.response.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LoginUser user = (LoginUser) request.getSession().getAttribute("user");
		if (user != null) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
}
