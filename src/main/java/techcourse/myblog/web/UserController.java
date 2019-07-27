package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.exception.AccessNotPermittedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static techcourse.myblog.service.exception.AccessNotPermittedException.PERMISSION_FAIL_MESSAGE;

@Controller
public class UserController {
    public static final String LOGGED_IN_USER = "loggedInUser";

    private UserService userService;
    private ArticleService articleService;

    public UserController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage() {
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
        Long updatedUserId;
        HttpSession httpSession = httpServletRequest.getSession();

        validateUser(httpSession, id);
        updatedUserId = userService.update(userProfileDto);
        userProfileDto.setId(id);
        httpSession.setAttribute(LOGGED_IN_USER, userProfileDto);
        return "redirect:/mypage/" + updatedUserId;
    }

    @Transactional
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();

        validateUser(httpSession, id);
        articleService.deleteByUserId(id);
        userService.deleteById(id);
        httpSession.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }

    private void validateUser(HttpSession httpSession, Long id) {
        UserProfileDto loggedInUser = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (!loggedInUser.getId().equals(id)) {
            throw new AccessNotPermittedException(PERMISSION_FAIL_MESSAGE);
        }
    }
}
