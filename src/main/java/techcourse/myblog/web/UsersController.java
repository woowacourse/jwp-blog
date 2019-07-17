package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
	@GetMapping("/signup")
	public String signUpPage() {
		return "signup";
	}

//	@PostMapping("users")
//	public String signUp(Model model) {
//
//	}
}
