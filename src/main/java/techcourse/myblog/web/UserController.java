package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
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
            modelAndView.setView(new RedirectView("signup"));
            return modelAndView;
        }
        modelAndView.setView(new RedirectView("login"));
        userService.save(userRequestDto);
        return modelAndView;
    }

    @PostMapping("/users/login")
    public ModelAndView processLogin(HttpServletRequest request, String email, String password) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto userResponseDto = null;
        try {
            userResponseDto = userService.findByEmailAndPassword(email, password);
        } catch (IllegalArgumentException e) {
            // TODO: 2019-07-19 에러메시지 띄우기
            System.err.println(e.getMessage());
            modelAndView.setView(new RedirectView("/login"));
            return modelAndView;
        }

        HttpSession session = request.getSession();
        session.setAttribute("name", userResponseDto.getName());
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }
}
