package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
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
    public String addUser(UserDto userDto, HttpSession session) {
        createSession(session, userService.addUser(userDto));
        return "redirect:/login";
    }

    @PutMapping("/users")
    public String updateUser(UserDto userDto, HttpSession session) {
        userService.updateUser(getSessionValueByEmail(session), userDto);
        createSession(session, userDto);
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser(HttpSession session) {
        userService.deleteUser(getSessionValueByEmail(session));
        removeSession(session);
        return "redirect:/";
    }

    private void createSession(HttpSession session, UserDto userDto) {
        session.setAttribute("userName", userDto.getName());
        session.setAttribute("userEmail", userDto.getEmail());
    }

    private void removeSession(HttpSession session) {
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
    }

    private String getSessionValueByEmail(HttpSession session) {
        return session.getAttribute("userEmail").toString();
    }

}
