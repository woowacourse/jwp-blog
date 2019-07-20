package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.domain.User.*;
import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
public class UserController {
    public static final String USER_MAPPING_URL = "/user";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
    public String show(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/show 에서의 로그 : "+ userDto.toString());
        model.addAttribute("user", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String editForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        log.info("/user/edit 에서의 로그 : "+ userDto.toString());
        model.addAttribute("user", userDto);
        model.addAttribute("namePattern",NAME_PATTERN);
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView edit(HttpServletRequest request,String name){
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto)session.getAttribute("user");
        userDto.setName(name);
        session.setAttribute("user",userDto);
        log.info(userDto.getName() + " "+ userDto.getEmail()+" "+ userDto.getPassword());
        userService.updateUserName(userDto, name);
        return new RedirectView(USER_MAPPING_URL+"/show");
    }

    @PostMapping("/login")
    public RedirectView login(UserDto userDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UserDto responseUserDto;
        log.info("/user/login 에서의 로그 " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        try {
            userService.checkPassword(userDto);
            log.info("패스워드 체크까지 했음");
            responseUserDto = userService.getUserDtoByEmail(userDto.getEmail());
            log.info("이메일 찾기 까지 했음");
            request.getSession().setAttribute("user",responseUserDto);
            return new RedirectView("/");
        } catch (IllegalArgumentException e){
            log.info("캐치블록까지 왔음");
            redirectAttributes.addAttribute("message","로긴실패!");
            RedirectView rv = new RedirectView();
            rv.setUrl(USER_MAPPING_URL);
            return rv;
        }
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
