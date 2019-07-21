package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.SingUpDto;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userName");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserRequestDto userRequestDto, Model model, HttpSession session) {
        LoginDto dto = userService.loginByEmailAndPwd(userRequestDto);
        if (dto.isSuccess()) {
            session.setAttribute("userName", dto.getName());
            session.setAttribute("userEmail", userRequestDto.getEmail());
            return "redirect:/";
        }
        model.addAttribute("error", dto.getMessage());
        return "login";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String editUser() {
        return "mypage-edit";
    }

    @PostMapping("/users")
    public String addUser(UserRequestDto userRequestDto, Model model, HttpSession session) {
        SingUpDto singUpDto = userService.addUser(userRequestDto);

        if (userService.addUser(userRequestDto).isSuccess()) {
            session.setAttribute("userName", userRequestDto.getName());
            return "redirect:/";
        }
        model.addAttribute("error", singUpDto.getMessage());
        return "signup";
    }

    @PutMapping("/users")
    public String updateUser(UserRequestDto userRequestDto, HttpSession session) {
        userService.updateUser(userRequestDto, session);
        session.setAttribute("userName", userRequestDto.getName());
        session.setAttribute("userEmail", userRequestDto.getEmail());
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser(HttpSession session) {
        userService.deleteUser(session);
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        return "redirect:/";
    }
}
