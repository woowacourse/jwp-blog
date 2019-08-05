package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {
	private UserService userService;
	private LoginChecker loginChecker;

	public MyPageController(UserService userService, LoginChecker loginChecker) {
		this.userService = userService;
		this.loginChecker = loginChecker;
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
		if (loginChecker.isLoggedInSameId(session, id)) {
			model.addAttribute("user", userService.findUserPublicInfoById(id));
			return "mypage-edit";
		}
		return "redirect:/mypage/" + id;
	}
}
