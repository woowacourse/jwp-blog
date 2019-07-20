package techcourse.myblog.web;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/signup")
	public String signUpPage(HttpSession httpSession) {
		if (httpSession.getAttribute("email") != null) {
			return "redirect:/";
		}
		return "signup";
	}

	@PostMapping("/users")
	public String signUp(@Valid UserDto newUser, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("errors", errors.get(0).getField() + "입력 오류 입니다.");
			return "/signup";
		}
		if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
			model.addAttribute("errors", "이미 존재하는 email입니다.");
			return "/signup";
		}

		User user = new User(newUser);
		userRepository.save(user);
		return "redirect:/login";
	}

	@GetMapping("/users")
	public String users() {
		return "redirect:/user-list";
	}

	@GetMapping("/user-list")
	public String userList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user-list";
	}

	@GetMapping("/mypage")
	public String mypage(HttpSession httpSession, Model model) {
		if (httpSession.getAttribute("email") == null) {
			return "redirect:/";
		}
		model.addAttribute("user", userRepository.findByEmail(httpSession.getAttribute("email").toString()).get());
		return "mypage";
	}

	@GetMapping("/mypage/edit")
	public String mypageEdit(HttpSession httpSession, Model model) {
		if (httpSession.getAttribute("email") == null) {
			return "redirect:/";
		}
		model.addAttribute("user", userRepository.findByEmail(httpSession.getAttribute("email").toString()).get());
		return "mypage-edit";
	}

	@GetMapping("/leave")
	public String leave(HttpSession httpSession) {
		if (httpSession.getAttribute("email") == null) {
			return "redirect:/";
		}
		return "leave-user";
	}

	@PostMapping("/leave")
	public String leaveUser(HttpSession httpSession, Model model, String password) {
		if (httpSession.getAttribute("email") == null) {
			return "redirect:/";
		}
		String email = httpSession.getAttribute("email").toString();
		Optional<User> user = userRepository.findByEmail(email);

		if(user.get().matchPassword(password)) {
			model.addAttribute("result", "회원 탈퇴가 완료되었습니다.");
			userRepository.delete(user.get());
			httpSession.invalidate();
			return "leave-user";
		}

		model.addAttribute("errors", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
		return "leave-user";
	}
}
