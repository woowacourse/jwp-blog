package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import techcourse.myblog.domain.LoginRequestDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.domain.UserRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String index(UserRequestDto userRequestDto) {
        return "signup";
    }

    @Transactional
    @PostMapping("/users")
    public String createUser(@Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userRepository.save(userRequestDto.toEntity());
        return "redirect:/login";
    }

    @ResponseBody
    @PostMapping("/check-email")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> checkEmail(String email) {
        int duplicate = userRepository.findUsersByEmail(email).size();

        if (duplicate > 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    public String selectAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, Model model, HttpSession httpSession) {
        User user = userRepository.findUserByEmail(loginRequestDto.getEmail());
        if (user == null) {
            model.addAttribute("error", true);
            model.addAttribute("message", "존재하지않는 email입니다.");
            return "login";
        }
        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            model.addAttribute("error", true);
            model.addAttribute("message", "비밀번호가 일치하지않습니다.");
            return "login";
        }
        if (httpSession.getAttribute("user") != null) {
            return "redirect:/";
        }
        httpSession.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
