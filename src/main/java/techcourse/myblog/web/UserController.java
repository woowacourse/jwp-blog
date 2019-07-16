package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.repo.UserRepository;

import javax.jws.WebParam;
import javax.persistence.GeneratedValue;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpPage(){
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(User user,Model model){
        //TODO 규칙
        if(userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error","중복");
            return "signup";
        }
        userRepository.save(user);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping
    public String findAllUsers(Model model){
        model.addAttribute("users",userRepository.findAll());
        return "user-list";
    }


}
