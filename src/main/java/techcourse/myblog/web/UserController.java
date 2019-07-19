package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UserCreationException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
	private static final Logger log =
			LoggerFactory.getLogger(UserController.class);

	private static final String SESSION_NAME = "userInfo";
	private static final String ERROR_MESSAGE_NAME = "errorMessage";

	private final UserService userService;

	@Autowired
	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String login(final HttpSession session) {
		if (isSessionNull(session)) {
			return "/user/login";
		}
		return "redirect:/";
	}

	private boolean isSessionNull(HttpSession session) {
		return session.getAttribute(SESSION_NAME) == null;
	}

	@PostMapping("/login")
	public String login(final UserDto.LoginInfo loginInfo, final HttpSession session, final Model model) {
		if (userService.canLogin(loginInfo)) {
			User user = userService.findByLoginInfo(loginInfo);
			log.debug("login user : {}", user);
			UserDto.SessionUserInfo sessionUserInfo = UserDto.SessionUserInfo.toDto(user);
			log.debug("session user : {}", sessionUserInfo);
			session.setAttribute(SESSION_NAME, sessionUserInfo);
			return "/index";
		}
		model.addAttribute(ERROR_MESSAGE_NAME, "비밀번호를 올바르게 입력하시거나 회원가입을 해주세요");
		return "/user/login";
	}

	@GetMapping("/logout")
	public String logout(final HttpSession session) {
		session.removeAttribute(SESSION_NAME);
		return "/index";
	}

	@GetMapping("/signup")
	public String signUp(final HttpSession session) {
		if (isSessionNull(session)) {
			return "/user/signup";
		}
		return "redirect:/";
	}

	@GetMapping("/users")
	public String find(final Model model) {
		final Iterable<User> users = userService.findAll();
		log.debug("users : {}", users);
		model.addAttribute("users", users);
		return "/user/user-list";
	}

	@PostMapping("/users")
	public String save(final UserDto.SignUpUserInfo signUpUserInfo, final Model model) {
		if (userService.exitsByEmail(signUpUserInfo)) {
			model.addAttribute(ERROR_MESSAGE_NAME, "이메일이 중복됩니다");
			return "/user/signup";
		}

		try {
			userService.save(signUpUserInfo);
			return "redirect:/login";
		} catch (UserCreationException e) {
			model.addAttribute(ERROR_MESSAGE_NAME, e.getMessage());
			return "/user/signup";
		}
	}

	@GetMapping("/users/{id}")
	public String myPage(@PathVariable Long id, final Model model, final HttpSession session) {
		UserDto.SessionUserInfo sessionUserInfo = (UserDto.SessionUserInfo) session.getAttribute(SESSION_NAME);

		log.debug("session value : {}", sessionUserInfo);
		log.debug("id : {}", id);

		if (isSessionMatch(id, sessionUserInfo)) {
			User user = userService.findById(id);
			model.addAttribute("user", user);
			log.debug("{} to /mypage", user);
			return "mypage";
		}
		return "redirect:/users";
	}

	private boolean isSessionMatch(@PathVariable Long id, UserDto.SessionUserInfo sessionUserInfo) {
		log.debug("id in isSessionMatch() : {}", id);
		log.debug("session in isSessionMatch() : {}", sessionUserInfo);
		return sessionUserInfo != null && sessionUserInfo.isSameId(id);
	}

	@GetMapping("/users/edit/{id}")
	public String editPage(@PathVariable Long id, final Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		log.debug("{} to /mypage-edit", user);
		return "mypage-edit";
	}

	@PutMapping("/users/edit")
	public String update(final UserDto.UpdateInfo updateInfo, final HttpSession session) {
		try {
			log.debug("updateInfo in update() : {}", updateInfo);
			User user = userService.update(updateInfo);
			session.setAttribute(SESSION_NAME, UserDto.SessionUserInfo.toDto(user));
			return "redirect:/users/" + user.getId();
		} catch (UserCreationException e) {
			log.trace("try-catch UserCreationException");
			return "redirect:/users/edit/" + updateInfo.getId();
		}
	}

	@DeleteMapping("/users/{email}")
	public String delete(@PathVariable String email, final HttpSession session) {
		userService.deleteByEmail(email);
		session.removeAttribute(SESSION_NAME);
		return "/index";
	}
}
