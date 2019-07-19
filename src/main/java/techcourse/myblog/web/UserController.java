package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "signup";
        }
        return "redirect:/";
    }

    @PostMapping("/users")
    public RedirectView createUser(
            @Valid UserDto.Create userDto, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        userService.save(userDto);
        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String readUsers(Model model) {
        List<UserDto.Response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/mypage/{userId}")
    public String renderMypage(@PathVariable long userId, Model model) {
        UserDto.Response user = userService.findById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/mypage/{userId}/edit")
    public String renderEditMypage(@PathVariable long userId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = (UserDto.Response) session.getAttribute("user");
        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/users/{userId}")
    public RedirectView updateUsers(
                        @PathVariable long userId,
                        HttpServletRequest request,
                        @Valid UserDto.Update userDto,
                        BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        UserDto.Response updatedUser = userService.update(userId, userDto);
        HttpSession session = request.getSession(false);
        session.setAttribute("user", updatedUser);
        return new RedirectView("/mypage/" + userId);
    }
}
