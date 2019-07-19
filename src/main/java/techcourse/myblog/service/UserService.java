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

    public void create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }
        User user = new User(userDto);
        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void update(UserUpdateRequestDto userUpdateRequestDto, HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        user.setUserName(userUpdateRequestDto.getUserName());
        httpSession.setAttribute("user", user);
    }

    @Transactional
    public void delete(HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        httpSession.removeAttribute("user");
        userRepository.delete(user);
    }
}
