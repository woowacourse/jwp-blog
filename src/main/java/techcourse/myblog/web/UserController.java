package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.service.exception.UserUpdateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Controller
public class UserController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private UserService userService;
    private ArticleService articleService;

    public UserController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) != null) {
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
    public String editUserName(@PathVariable Long id, UserProfileDto userProfileDto, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (isLoggedInUser(httpSession, id)) {
            userService.update(userProfileDto);
            userProfileDto.setId(id);
            httpSession.setAttribute(LOGGED_IN_USER, userProfileDto);
        }
        return "redirect:/mypage/" + id;
    }

    @Transactional
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (isLoggedInUser(httpSession, id)) {
            articleService.deleteByUserId(id);
            userService.delete(id);
            httpSession.removeAttribute(LOGGED_IN_USER);
        }
        return "redirect:/";
    }

    private boolean isLoggedInUser(HttpSession httpSession, Long id) {
        UserProfileDto loggedInUser = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        return (loggedInUser != null) && loggedInUser.getId().equals(id);
    }

    @ExceptionHandler(NotFoundUserException.class)
    public String handleNotFoundUserException(Model model, Exception e) {
        return "redirect:/";
    }

    @ExceptionHandler(SignUpException.class)
    public String handleSignUpException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserUpdateException.class)
    public String handleUpdateUserException(Exception e,
                                            HttpServletRequest httpServletRequest,
                                            RedirectAttributes redirectAttributes) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserProfileDto user = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/mypage/" + user.getId() + "/edit";
    }
}
