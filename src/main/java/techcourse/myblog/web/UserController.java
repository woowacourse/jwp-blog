package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.user.UserRequestDto;
import techcourse.myblog.dto.user.UserResponseDto;
import techcourse.myblog.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ModelAndView showSignUp(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (!Objects.isNull(session.getAttribute("user"))) {
            modelAndView.setView(new RedirectView("/"));
            return modelAndView;
        }
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserResponseDto> userResponseDtos = userService.findAll();
        model.addAttribute("users", userResponseDtos);
        return "user-list";
    }

    @PostMapping("/users")
    public ModelAndView registerUsers(final UserRequestDto userRequestDto) {
        userService.save(userRequestDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/login"));
        return modelAndView;
    }

    @GetMapping("/mypage")
    public ModelAndView showMyPage(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage");
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @DeleteMapping("/mypage")
    public ModelAndView deleteUser(final HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        userService.delete(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/logout"));
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logOut(HttpServletRequest request) {
        request.getSession().invalidate();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }

    @GetMapping("/mypage/mypage-edit")
    public ModelAndView showMyPageEdit(final HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("mypage-edit");
        return modelAndView;
    }

    @PutMapping("/mypage/mypage-edit")
    public ModelAndView editMyPage(final HttpSession session, final String name) {
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        session.setAttribute("user", userService.update(user.getEmail(), name));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/mypage"));
        return modelAndView;
    }
}
