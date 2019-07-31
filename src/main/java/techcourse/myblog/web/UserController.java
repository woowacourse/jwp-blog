package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
	private static final String LOGGED_IN_USER = "loggedInUser";

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users/sign-up")
	public String showRegisterPage(HttpSession session) {
		if (session.getAttribute(LOGGED_IN_USER) != null) {
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
		if (isLoggedInUser(session, id)) {
			userService.update(userPublicInfoDto);
			session.setAttribute(LOGGED_IN_USER, userPublicInfoDto);
		}
		return "redirect:/mypage/" + id;
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable Long id, HttpSession session) {
		if (isLoggedInUser(session, id)) {
			userService.delete(id);
			session.invalidate();
		}
		return "redirect:/";
	}

	private boolean isLoggedInUser(HttpSession httpSession, Long id) {
		UserPublicInfoDto loggedInUser = (UserPublicInfoDto) httpSession.getAttribute(LOGGED_IN_USER);
		return (loggedInUser != null) && loggedInUser.getId().equals(id);
	}
}
