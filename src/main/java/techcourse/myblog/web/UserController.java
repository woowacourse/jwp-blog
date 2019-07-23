package techcourse.myblog.web;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.dto.request.UserEditProfileDto;
import techcourse.myblog.service.UserService;

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
	public String signUpPage(HttpSession httpSession) {
		if (!confirmSession(httpSession, "email")) {
			return "redirect:/";
		}
		return "signup";
	}

	@PostMapping("/users")
	public String signUp(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("error", errors.get(0).getField() + "입력 오류 입니다.");
			return "/signup";
		}
		userService.signUp(userDto);
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
		if (getUserInformation(httpSession, model)) {
			return "redirect:/";
		}
		return "mypage";
	}

	@GetMapping("/mypage/edit")
	public String mypageEdit(HttpSession httpSession, Model model) {
		if (getUserInformation(httpSession, model)) {
			return "redirect:/";
		}
		return "mypage-edit";
	}

	private boolean getUserInformation(HttpSession httpSession, Model model) {
		if (confirmSession(httpSession, "email")) {
			return true;
		}
		User user = userService.findUser((String) httpSession.getAttribute("email"));
		model.addAttribute("user", user);
		return false;
	}

	@PutMapping("/edit")
	public String editUser(@Valid UserEditProfileDto userEditProfileDto, HttpSession httpSession) {
		userService.editUser((String) httpSession.getAttribute("email"), userEditProfileDto);
		return "redirect:/mypage";
	}

	@GetMapping("/leave")
	public String leave(HttpSession httpSession) {
		if (confirmSession(httpSession, "email")) {
			return "redirect:/";
		}
		return "leave-user";
	}


	@DeleteMapping("/leave")
	public String leaveUser(HttpSession httpSession, Model model, String password) {
		if (confirmSession(httpSession, "email")) {
			return "redirect:/";
		}
		userService.leaveUser((String) httpSession.getAttribute("email"), password);
		model.addAttribute("result", "회원 탈퇴가 완료되었습니다.");
		httpSession.invalidate();
		return "leave-user";
	}

	private boolean confirmSession(HttpSession httpSession, String attribute) {
		return (httpSession.getAttribute("email") == null);
	}
}
