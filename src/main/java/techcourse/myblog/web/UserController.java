package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
	private UserService userService;
	private LoginChecker loginChecker;

	public UserController(UserService userService, LoginChecker loginChecker) {
		this.userService = userService;
		this.loginChecker = loginChecker;
	}

	@GetMapping("/users/sign-up")
	public String showRegisterPage(HttpSession session) {
		if (loginChecker.isLoggedIn(session)) {
			return "redirect:/";
		}
		return "sign-up";
	}

	@GetMapping("/users")
	public String showUserList(Model model) {
		model.addAttribute("users", userService.findAll());
		return "user-list";
	}

	@PostMapping("/users")
	public String createUser(UserDto userDto) {
		userService.save(userDto);
		return "redirect:/login";
	}

	@PutMapping("/users/{id}")
	public String editUserName(@PathVariable Long id, UserPublicInfoDto userPublicInfoDto, HttpSession session) {
		if (loginChecker.isLoggedInSameId(session, id)) {
			userService.update(userPublicInfoDto);
			session.setAttribute(LoginChecker.LOGGED_IN_USER, userPublicInfoDto);
		}
		return "redirect:/mypage/" + id;
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable Long id, HttpSession session) {
		if (loginChecker.isLoggedInSameId(session, id)) {
			userService.delete(id);
			session.removeAttribute(LoginChecker.LOGGED_IN_USER);
		}
		return "redirect:/";
	}
}
