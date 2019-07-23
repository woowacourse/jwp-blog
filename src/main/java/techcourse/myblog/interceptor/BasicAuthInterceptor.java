package techcourse.myblog.interceptor;

import java.nio.charset.Charset;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import techcourse.myblog.dto.request.UserLoginDto;
import techcourse.myblog.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BasicAuthInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private LoginService loginService;

	public BasicAuthInterceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) {
		String authorization = request.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Basic")) {
			return true;
		}

		String base64Credential =
				authorization.substring("Basic".length()).trim();

		String credentials =
				new String(Base64.getDecoder().decode(base64Credential),
						Charset.forName("UTF-8"));

		final String[] values = credentials.split(":", 2);

		UserLoginDto userLoginDto = new UserLoginDto();
		userLoginDto.setEmail(values[0]);
		userLoginDto.setPassword(values[1]);

		loginService.login(userLoginDto);
		return true;
	}
}
