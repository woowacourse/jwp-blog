package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User.User;
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

	@GetMapping("/users")
	public String find(final Model model) {
		final Iterable<User> users = userService.findAll();
		log.debug("users : {}", users);
		model.addAttribute("users", users);
		return "/user/user-list";
	}

	@PostMapping("/users")
	public String save(final UserRequestDto.SignUpRequestDto signUpRequestDto, final Model model) {
		if (userService.exitsByEmail(signUpRequestDto)) {
			model.addAttribute(ERROR_MESSAGE_NAME, "이메일이 중복됩니다");
			return "redirect:/signup";
		}

		try {
			userService.save(signUpRequestDto);
			return "redirect:/login";
		} catch (UserCreationException e) {
			model.addAttribute(ERROR_MESSAGE_NAME, e.getMessage());
			return "redirect:/signup";
		}
	}

	@GetMapping("/users/{id}")
	public String myPage(@PathVariable Long id, final Model model, final HttpSession session) {
		UserRequestDto.UserSessionDto userSessionDto = (UserRequestDto.UserSessionDto) session.getAttribute(SESSION_NAME);

		log.debug("session value : {}", userSessionDto);
		log.debug("id : {}", id);

		if (isSessionMatch(id, userSessionDto)) {
			User user = userService.findById(id);
			model.addAttribute("user", user);
			log.debug("{} to /mypage", user);
			return "mypage";
		}
		return "redirect:/users";
	}

	private boolean isSessionMatch(@PathVariable Long id, UserRequestDto.UserSessionDto userSessionDto) {
		log.debug("id in isSessionMatch() : {}", id);
		log.debug("session in isSessionMatch() : {}", userSessionDto);
		return userSessionDto != null && userSessionDto.isSameId(id);
	}

	@GetMapping("/users/edit/{id}")
	public String editPage(@PathVariable Long id, final Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		log.debug("{} to /mypage-edit", user);
		return "mypage-edit";
	}

	@PutMapping("/users/edit")
	public String update(final UserRequestDto.UpdateRequestDto updateRequestDto, final HttpSession session) {
		try {
			log.debug("updateRequestDto in update() : {}", updateRequestDto);
			User user = userService.update(updateRequestDto);
			session.setAttribute(SESSION_NAME, UserRequestDto.UserSessionDto.toDto(user));
			return "redirect:/users/" + user.getId();
		} catch (UserCreationException e) {
			log.trace("try-catch UserCreationException");
			return "redirect:/users/edit/" + updateRequestDto.getId();
		}
	}

	@DeleteMapping("/users/{email}")
	public String delete(@PathVariable String email, final HttpSession session) {
		userService.deleteByEmail(email);
		session.removeAttribute(SESSION_NAME);
		return "redirect:/";
	}
}
