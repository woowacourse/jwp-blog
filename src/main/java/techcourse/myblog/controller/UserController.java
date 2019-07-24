package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

import static techcourse.myblog.controller.UserController.USER_MAPPING_URL;
import static techcourse.myblog.dto.UserDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
@Slf4j
public class UserController {
    public static final String USER_MAPPING_URL = "/user";
    private static final String DUPLICATE_EMAIL_ERROR_MESSAGE = "존재하는 이메일입니다.";
    private static final String NO_EMAIL_ERROR_MESSAGE = "가입된 이메일이 없습니다.";
    public final UserService userService;

    @GetMapping
    public String loginForm() {
        log.debug("/user : loginForm");
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
    public ModelAndView signUp(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return addErrorMessageToModel(bindingResult.getFieldError().getDefaultMessage());
        }

        Optional<UserDto> maybeUserDto = userService.getUserDtoByEmail(userDto.getEmail());
        log.debug("signup post {}", userDto);
        if (maybeUserDto.isPresent()) {
            return addErrorMessageToModel(DUPLICATE_EMAIL_ERROR_MESSAGE);
        }
        userService.save(userDto);
        return new ModelAndView("redirect:" + USER_MAPPING_URL);
    }

    @PostMapping("/login")
    public ModelAndView login(UserDto userDto, HttpSession session) {
        log.debug("/login post : {}", userDto);
        Optional<UserDto> maybeUserDto = userService.getUserDtoByEmail(userDto.getEmail());

        if (!maybeUserDto.isPresent()) { //아이디 존재하지 않으면
            return addErrorMessageToModel(NO_EMAIL_ERROR_MESSAGE);
        }

        try {
            userService.checkPassword(userDto);
            log.debug("/login post : from userService.checkPassword");
            session.setAttribute("user", maybeUserDto.get());
            return new ModelAndView("redirect:/");
        } catch (IllegalArgumentException e) {
            log.debug("/login post : catch block");
            return addErrorMessageToModel(e.getMessage());
        }
    }

    @GetMapping("/show")
    public String show(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.debug("/show get : {}" + userDto);
        model.addAttribute("user", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.debug("/edit get : {}", userDto);
        model.addAttribute("namePattern", NAME_PATTERN);
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public String edit(HttpSession session, String name) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        userDto.setName(name);
        session.setAttribute("user", userDto);
        log.debug("/edit put : {}", userDto);
        userService.updateUserName(userDto);
        return "redirect:/" + USER_MAPPING_URL + "/show";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @DeleteMapping("/delete")
    public String delete(HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.debug("/delete delete : {}", userDto);
        session.removeAttribute("user");
        userService.deleteUser(userDto);
        return "redirect:/";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    private ModelAndView addErrorMessageToModel(String errorMessage) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", errorMessage);
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
