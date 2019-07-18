package techcourse.myblog.web;

import javax.validation.Valid;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@PostMapping("users")
	public String signUp(@Valid UserDto newUser, Model model) {
		System.out.println(newUser);
		User user = new User(newUser);
		userRepository.save(user);
		return "login";
	}
}
