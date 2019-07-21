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
    private static final String DUPLICATE_EMAIL_ERROR_MESSAGE = "존재하는 이메일입니다.";
    private static final String NO_EMAIL_ERROR_MESSAGE = "가입된 이메일이 없습니다.";
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
        Optional<UserDto> maybeUserDto = userService.getUserDtoByEmail(userDto.getEmail());
        log.info("Controller-signUp Post");
        if (maybeUserDto.isPresent()) {
            return getAndInsertModelAndView(DUPLICATE_EMAIL_ERROR_MESSAGE);
        }
        userService.save(userDto);
        return new ModelAndView("redirect:/" + USER_MAPPING_URL);
    }

    @PostMapping("/login")
    public ModelAndView login(UserDto userDto, HttpSession session) {
        log.info("/user/login 에서의 로그 " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        Optional<UserDto> maybeUserDto = userService.getUserDtoByEmail(userDto.getEmail());

        if (!maybeUserDto.isPresent()) {
            log.info("Contoller-login : 중복 이메일 체크");
            return getAndInsertModelAndView(NO_EMAIL_ERROR_MESSAGE);
        }

        try {
            userService.checkPassword(userDto);
            log.info(" Contoller-login : 패스워드 체크까지 했음");
            session.setAttribute("user", maybeUserDto.get());
            return new ModelAndView("redirect:/");
        } catch (IllegalArgumentException e) {
            log.info("Contoller-login : 캐치블록");
            return getAndInsertModelAndView(e.getMessage());
        }
    }

    @GetMapping("/show")
    public String show(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/show 에서의 로그 : " + userDto.toString());
        model.addAttribute("user", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/edit 에서의 로그 : " + userDto.toString());
        model.addAttribute("namePattern", NAME_PATTERN);
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView edit(HttpSession session, String name) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        userDto.setName(name);
        session.setAttribute("user", userDto);
        log.info(userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
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
        log.info("delete 까지 옴 : " + userDto.toString());
        session.removeAttribute("user");
        userService.deleteUser(userDto);
        return new RedirectView("/");
    }

    private ModelAndView getAndInsertModelAndView(String errorMessage) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", errorMessage);
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
