package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.domain.UserRequestDto;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/users")
    public String addUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(userRequestDto.toUser());
        System.out.println(user);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUserListPage(Model model) {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(user);
        }

        model.addAttribute("users", users.stream()
                .map(user -> UserRequestDto.from(user))
                .collect(toList()));

        System.out.println(users);

        return "user-list";
    }
}
