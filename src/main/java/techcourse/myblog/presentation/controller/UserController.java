package techcourse.myblog.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static techcourse.myblog.presentation.controller.UserController.USER_MAPPING_URL;
import static techcourse.myblog.domain.User.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
@Slf4j
public class UserController {
    public static final String USER_MAPPING_URL = "/user";
    public final UserService userService;

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("emailPattern", EMAIL_PATTERN);
        model.addAttribute("namePattern", NAME_PATTERN);
        model.addAttribute("passwordPattern", PASSWORD_PATTERN);
        return "signup";
    }

    @PostMapping
    public ModelAndView signUp(UserDto userDto) {
        userService.save(userDto);
        return new ModelAndView("redirect:/" + USER_MAPPING_URL);
    }

    @PostMapping("/login")
    public ModelAndView login(UserDto userDto, HttpSession session) {
        Optional<UserDto> maybeUserDto = userService.getUserDtoByEmail(userDto.getEmail());
        try {
            userService.checkPassword(userDto);
            session.setAttribute("user", maybeUserDto.get());
            return new ModelAndView("redirect:/");
        } catch (IllegalArgumentException e) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("error", "없는 이메일이거나 틀린 비밀번호 입니다.");
            modelAndView.setViewName("login");
            return modelAndView;
        }
    }

    @GetMapping("/show")
    public String show(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        model.addAttribute("user", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        model.addAttribute("user",userDto);
        model.addAttribute("namePattern", NAME_PATTERN);
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView edit(HttpSession session, String name) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        userDto.setName(name);
        session.setAttribute("user", userDto);
        userService.updateUserName(userDto, name);
        return new RedirectView(USER_MAPPING_URL + "/show");
    }

    @PostMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }

    @DeleteMapping("/delete")
    public RedirectView delete(HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        session.removeAttribute("user");
        userService.deleteUser(userDto);
        return new RedirectView("/");
    }

}
