package techcourse.myblog.web;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.service.UserService;
import techcourse.myblog.user.UserChangeableInfo;
import techcourse.myblog.user.UserSignUpInfo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {
	private final UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String signUpPage() {
		return "signup";
	}

	@PostMapping("/users")
	public String signUp(@Valid UserSignUpInfo userSignUpInfo, BindingResult bindingResult, Model model) {
		if (confirmBindingErrors(bindingResult, model)) {
			return "/signup";
		}
		userService.signUp(userSignUpInfo);
		return "redirect:/login";
	}

	@GetMapping("/users")
	public String users() {
		return "redirect:/user-list";
	}

	@GetMapping("/user-list")
	public String userList(Model model) {
		model.addAttribute("users", userService.findAll());
		return "user-list";
	}

	@GetMapping("/mypage")
	public String mypage(HttpSession httpSession, Model model) {
		String email = (String) httpSession.getAttribute("email");
		model.addAttribute(userService.findUser(email));
		return "mypage";
	}

	@GetMapping("/mypage/edit")
	public String mypageEdit(HttpSession httpSession, Model model) {
		String email = (String) httpSession.getAttribute("email");
		model.addAttribute(userService.findUser(email));
		return "mypage-edit";
	}

	@PutMapping("/edit")
	public String editUser(@Valid UserChangeableInfo userChangeableInfo, BindingResult bindingResult, HttpSession httpSession, Model model) {
		if (confirmBindingErrors(bindingResult, model)) {
			return "index";
		}
		userService.editUser((String) httpSession.getAttribute("email"), userChangeableInfo);
		return "redirect:/mypage";
	}

	@GetMapping("/leave")
	public String leave() {
		return "leave-user";
	}


	@DeleteMapping("/leave")
	public String leaveUser(HttpSession httpSession, Model model, String password) {
		userService.leaveUser((String) httpSession.getAttribute("email"), password);
		model.addAttribute("result", "회원 탈퇴가 완료되었습니다.");
		httpSession.invalidate();
		return "leave-user";
	}

	private boolean confirmBindingErrors(BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("error", errors.get(0).getField() + "입력 오류 입니다.");
			return true;
		}
		return false;
	}
}
