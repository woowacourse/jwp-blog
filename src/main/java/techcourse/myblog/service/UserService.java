package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return false;
        }
        User user = new User(userDto);
        userRepository.save(user);
        return true;
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public String update(UserUpdateRequestDto userUpdateRequestDto, HttpSession httpSession) {
        try {
            String email = ((User) httpSession.getAttribute("user")).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            user.setUserName(userUpdateRequestDto.getUserName());
            httpSession.setAttribute("user", user);
            return "redirect:/users/mypage";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }

    @Transactional
    public String delete(HttpSession httpSession) {
        try {
            String email = ((User) httpSession.getAttribute("user")).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            httpSession.removeAttribute("user");
            userRepository.delete(user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }
}
