package techcourse.myblog.web;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserLoginDto;
import techcourse.myblog.service.LoginService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/login")
	public String login(HttpSession httpSession) {
		if (!confirmSession(httpSession)) {
			return "redirect:/";
		}
		return "login";
	}

	private boolean confirmSession(HttpSession httpSession) {
		return httpSession.getAttribute("email") == null;
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
}
