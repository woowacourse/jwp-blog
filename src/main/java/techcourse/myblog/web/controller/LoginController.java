package techcourse.myblog.web.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.request.UserLoginDto;
import techcourse.myblog.web.supports.LoginUserResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller
public class LoginController {
	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String userLogin(@Valid UserLoginDto userLoginDto, BindingResult bindingResult, Model model, HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("error", errors.get(0).getField() + "입력 오류 입니다.");
			return "login";
		}

		User loginUser = loginService.login(userLoginDto);
		httpSession.setAttribute("email", loginUser.getEmail());
		httpSession.setAttribute("username", loginUser.getUsername());
		return "redirect:/user-list";
	}

	@Configuration
	public static class WebMvcConfig implements WebMvcConfigurer {
		@Bean
		public AuthenticationInterceptor basicAuthInterceptor() {
			return new AuthenticationInterceptor();
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(basicAuthInterceptor())
					.addPathPatterns("/**")
					.excludePathPatterns("/")
					.excludePathPatterns("/css/**")
					.excludePathPatterns("/js/**")
					.excludePathPatterns("/images/**");
		}

		@Bean
		public LoginUserResolver loginUserResolver() {
			return new LoginUserResolver();
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(loginUserResolver());
			WebMvcConfigurer.super.addArgumentResolvers(resolvers);
		}
	}

	public static class AuthenticationInterceptor extends HandlerInterceptorAdapter {
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
			if ((!"/".equals(path)) && (!"/signup".equals(path)) && (!"/login".equals(path)) && (!"/users".equals(path))) {
				response.sendRedirect("/login");
				return false;
			}
			return true;
		}

		private boolean isAccessibleLoginUser(HttpServletResponse response, String path) throws IOException {
			if ("/login".equals(path) || "/signup".equals(path)) {
				response.sendRedirect("/");
				return false;
			}
			return true;
		}
	}
}
