package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.EmailNotFoundException;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (!Objects.isNull(session.getAttribute("user"))) {
            modelAndView.setView(new RedirectView("/"));
            return modelAndView;
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView showSignUp(final HttpSession session, UserRequestDto userRequestDto) {
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
    public ModelAndView registerUsers(@Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/signup");
            return modelAndView;
        }
        try {
            userService.checkPasswordAndRepassword(userRequestDto);
        } catch (Exception e) {
            bindingResult.addError(new FieldError("userRequestDto", "rePassword", e.getMessage()));
            modelAndView.setViewName("/signup");
            return modelAndView;
        }
        try {
            userService.checkEmail(userRequestDto);
        } catch (Exception e) {
            bindingResult.addError(new FieldError("userRequestDto", "email", e.getMessage()));
            modelAndView.setViewName("/signup");
            return modelAndView;
        }

        modelAndView.setView(new RedirectView("/login"));
        userService.save(userRequestDto);
        return modelAndView;
    }

    @PostMapping("/users/login")
    public ModelAndView processLogin(HttpServletRequest request, String email, String password) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto userResponseDto = null;
        try {
            userResponseDto = userService.findByEmailAndPassword(email, password);
        } catch (EmailNotFoundException | InvalidPasswordException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("login");
            return modelAndView;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", userResponseDto);
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }

    @GetMapping("/mypage")
    public ModelAndView showMyPage(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage");
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        modelAndView.addObject("user", user);
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
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("mypage-edit");
        return modelAndView;
    }

    @PutMapping("/mypage/mypage-edit")
    public ModelAndView editMyPage(final HttpSession session, final String name, final String email) {
        System.err.println(name + "     " + email);
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto user = userService.update(name, email);
        session.setAttribute("user", user);
        modelAndView.setView(new RedirectView("/mypage"));
        return modelAndView;
    }

    @DeleteMapping("/mypage")
    public ModelAndView deleteUser(final HttpSession session) {
        UserResponseDto userResponseDto = (UserResponseDto) session.getAttribute("user");
        userService.deleteUser(userResponseDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/logout"));
        return modelAndView;
    }
}
