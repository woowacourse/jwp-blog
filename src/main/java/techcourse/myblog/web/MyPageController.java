package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {
	private static final String LOGGED_IN_USER = "loggedInUser";

	private UserService userService;

	public MyPageController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/mypage/{id}")
	public String showMyPage(@PathVariable("id") long id, Model model) {
		model.addAttribute("user", userService.findUserPublicInfoById(id));
		return "mypage";
	}

	@GetMapping("/mypage/{id}/edit")
	public String showMyPageEdit(@PathVariable("id") long id, Model model,
	                             HttpSession session, RedirectAttributes redirectAttributes) {
		String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
		if (errorMessage != null) {
			model.addAttribute("errorMessage", errorMessage);
		}
		if (isLoggedInUserMyPage(session, id)) {
			model.addAttribute("user", userService.findUserPublicInfoById(id));
			return "mypage-edit";
		}
		return "redirect:/mypage/" + id;
	}

	private boolean isLoggedInUserMyPage(HttpSession session, Long id) {
		UserPublicInfoDto loggedInUser = (UserPublicInfoDto) session.getAttribute(LOGGED_IN_USER);
		return (loggedInUser != null) && (id.equals(loggedInUser.getId()));
	}
}
