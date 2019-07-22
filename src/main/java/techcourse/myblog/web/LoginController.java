package techcourse.myblog.web;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserLoginDto;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	private final UserRepository userRepository;

	public LoginController(UserRepository userRepository) {
		this.userRepository = userRepository;
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
			model.addAttribute("errors", errors.get(0).getField() + "입력 오류 입니다.");
			return "login";
		}
		Optional<User> loginUser = userRepository.findByEmail(userLoginDto.getEmail());
		if (!loginUser.isPresent()) {
			model.addAttribute("errors", "없는 아이디입니다. 회원가입을 진행해주세요.");
			return "login";
		}
		if (!loginUser.get().matchPassword(userLoginDto.getPassword())) {
			model.addAttribute("errors", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
			return "login";
		}

		httpSession.setAttribute("email", loginUser.get().getEmail());
		httpSession.setAttribute("username", loginUser.get().getUsername());
		return "redirect:/user-list";
	}
}
