package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.controller.UserController.USER_MAPPING_URL;
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
    public RedirectView signUp(UserDto userDto) {
        userService.save(userDto);
        return new RedirectView(USER_MAPPING_URL);
    }

    @GetMapping("/show")
    public String show(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/show 에서의 로그 : "+ userDto.toString());
        model.addAttribute("user", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/edit 에서의 로그 : "+ userDto.toString());
        model.addAttribute("user", userDto);
        model.addAttribute("namePattern",NAME_PATTERN);
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView edit(HttpSession session,String name){
        UserDto userDto = (UserDto)session.getAttribute("user");
        userDto.setName(name);
        session.setAttribute("user",userDto);
        log.info(userDto.getName() + " "+ userDto.getEmail()+" "+ userDto.getPassword());
        userService.updateUserName(userDto, name);
        return new RedirectView(USER_MAPPING_URL+"/show");
    }

    @PostMapping("/login")
    public ModelAndView login(UserDto userDto, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDto responseUserDto;
        log.info("/user/login 에서의 로그 " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        try {
            responseUserDto = userService.getUserDtoByEmail(userDto.getEmail());
            log.info("이메일 찾기 까지 했음");
            userService.checkPassword(userDto);
            log.info("패스워드 체크까지 했음");
            session.setAttribute("user",responseUserDto);
            return new ModelAndView("redirect:/");
        } catch (IllegalArgumentException e){
            log.info("캐치블록까지 왔음");
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("error",e.getMessage());
            modelAndView.setViewName("login");
            return modelAndView;        }
    }

    @PostMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }

    @DeleteMapping("/delete")
    public RedirectView delete(HttpSession session){
        UserDto userDto = (UserDto)session.getAttribute("user");
        log.info("delete 까지 옴 : "+ userDto.toString());
        session.removeAttribute("user");
        userService.deleteUser(userDto);
        return new RedirectView("/");
    }
}
