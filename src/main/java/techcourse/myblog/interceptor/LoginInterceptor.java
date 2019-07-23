package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.web.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final HttpSession session = request.getSession();
		final String uri = request.getRequestURI();
		final String methodName = request.getMethod();
		log.info("preHandle... {} : {}", LoginInterceptor.class, methodName, uri);

		Object sessionDto = session.getAttribute(Constants.SESSION_USER_NAME);
		final boolean isLogin = !ObjectUtils.isEmpty(sessionDto);

		if (isLogin) {
			response.sendRedirect("/");
			return false;
		}

		return true;
	}
}
