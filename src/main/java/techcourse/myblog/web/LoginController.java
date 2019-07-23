package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
	private static final Logger log =
			LoggerFactory.getLogger(LoginController.class);

	private static final String ERROR_MESSAGE_NAME = "errorMessage";

	private final LoginService loginService;

	@Autowired
	public LoginController(final LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}

	@PostMapping("/login")
	public String login(final UserRequestDto.LoginRequestDto loginRequestDto, final HttpSession session) {
		log.info("login...");
		User user = loginService.login(loginRequestDto);
		UserRequestDto.SessionDto sessionDto = UserRequestDto.SessionDto.toDto(user);
		log.debug("user {} transform to {}", user, sessionDto);
		session.setAttribute(Constants.SESSION_USER_NAME, sessionDto);
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(final HttpSession session) {
		session.removeAttribute(Constants.SESSION_USER_NAME);
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String signUp() {
		return "/user/signup";
	}
}
