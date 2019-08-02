package techcourse.myblog.web;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.custom.LoginUser;
import techcourse.myblog.dto.request.UserChangeableInfoDto;
import techcourse.myblog.dto.request.UserSignUpInfoDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.user.User;

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
	public String signUp(@Valid UserSignUpInfoDto userSignUpInfoDto, BindingResult bindingResult, Model model) {
		if (confirmBindingErrors(bindingResult, model)) {
			return "/signup";
		}
		userService.signUp(userSignUpInfoDto);
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
	public String mypage(@LoginUser User user, Model model) {
		model.addAttribute(userService.findUser(user));
		return "mypage";
	}

	@GetMapping("/mypage/edit")
	public String mypageEdit(@LoginUser User user, Model model) {
		model.addAttribute(userService.findUser(user));
		return "mypage-edit";
	}

	@PutMapping("/edit")
	public String editUser(@LoginUser User user, @Valid UserChangeableInfoDto userChangeableInfoDto, BindingResult bindingResult, Model model) {
		if (confirmBindingErrors(bindingResult, model)) {
			return "index";
		}
		userService.editUser(user, userChangeableInfoDto);
		return "redirect:/mypage";
	}

	@GetMapping("/leave")
	public String leave() {
		return "leave-user";
	}


	@DeleteMapping("/leave")
	public String leaveUser(@LoginUser User user, Model model, String password, HttpSession httpSession) {
		userService.leaveUser(user, password);
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
