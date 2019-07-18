package techcourse.myblog.web;

import java.util.List;
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
public class UsersController {
	private final UserRepository userRepository;

	public UsersController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/signup")
	public String signUpPage() {
		return "signup";
	}

	@PostMapping("/users")
	public String signUp(@Valid UserDto newUser, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("errors", errors.get(0).getField()+"입력 오류 입니다.");
			return "/signup";
		}
		System.out.println(newUser);
		User user = new User(newUser);
		userRepository.save(user);
		return "redirect:/login";
	}
}
