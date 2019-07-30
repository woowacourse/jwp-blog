package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.dto.LoginDto;
import techcourse.myblog.web.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
	private static String REDIRECT_INDEX = "redirect:/";
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String singUp(UserDto userDto, HttpSession httpSession) {
		if (hasSession(httpSession)) return REDIRECT_INDEX;
		return "signup";
	}

	@GetMapping("/login")
	public String loginForm(HttpSession httpSession, LoginDto loginDto) {
		if (hasSession(httpSession)) return REDIRECT_INDEX;
		return "login";
	}

	private boolean hasSession(HttpSession httpSession) {
		return httpSession.getAttribute("user") != null;
	}

	@PostMapping("/login")
	public String login(@Valid LoginDto loginDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
		if (bindingResult.hasErrors()) {
			return "login";
		}
		httpSession.setAttribute("user", userService.login(loginDto));
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/";
	}

	@GetMapping("/mypage")
	public String showMyPage(HttpSession httpSession) {
		return "mypage";
	}

	@GetMapping("/mypage/edit")
	public String showMyPageEdit(HttpSession httpSession) {
		return "mypage-edit";
	}

	@PutMapping("/mypage/edit")
	public String updateUser(UserDto userDto, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		userDto.setEmail(user.getEmail());
		httpSession.setAttribute("user", userService.update(userDto));

		return "redirect:/mypage";
	}

	@DeleteMapping("/mypage/edit")
	public String deleteUser(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		userService.remove(user.getEmail());
		return "redirect:/logout";
	}

	@GetMapping("/users")
	public String users(Model model, HttpSession httpSession) {
		model.addAttribute("users", userService.findAll());
		return "user-list";
	}

	@PostMapping("/users")
	public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		if (userService.exists(userDto.getEmail())) {
			model.addAttribute("errorMessage", "중복된 이메일입니다.");
			return "signup";
		}
		userService.save(userDto);
		return "redirect:/login";
	}
}
