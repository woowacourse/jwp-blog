package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.NotFoundUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private UserRepository userRepository;

    public MyPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/mypage/{id}")
    public String showMyPage(@PathVariable("id") long id, Model model, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);
        model.addAttribute("user", new UserProfileDto(user.getId(), user.getName(), user.getEmail()));
        return "mypage";
    }

    @GetMapping("/mypage/{id}/edit")
    public String showMyPageEdit(@PathVariable("id") long id, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);
        if (isLoggedInUserMYPage(httpServletRequest, user)) {
            model.addAttribute("user", new UserProfileDto(user.getId(), user.getName(), user.getEmail()));
            return "mypage-edit";
        }
        return "redirect:/mypage/" + id;
    }

    private boolean isLoggedInUserMYPage(HttpServletRequest httpServletRequest, User user) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserProfileDto loggedInUser = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        return (loggedInUser != null) && (user.getId().equals(loggedInUser.getId()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public String handleNotFoundUserException(Model model, Exception e) {
        return "redirect:/";
    }
}
