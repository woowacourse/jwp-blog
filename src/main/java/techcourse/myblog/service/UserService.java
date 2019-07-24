package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.UnequalPasswordException;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService {
    private static final String LOGIN_SESSION_KEY = "loginUser";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        checkEqualPassword(userDto);
        checkDuplicateEmail(userDto.getEmail());
        User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);
    }

    private void checkEqualPassword(UserDto userDto) {
        if (!userDto.isEqualInputPassword()) {
            throw new UnequalPasswordException();
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional
    public void update(User user, UserUpdateDto userUpdateDto) {
        user.updateUser(userUpdateDto.getName());
    }

    public void delete(HttpSession session) {
        Long userId = ((User) session.getAttribute(LOGIN_SESSION_KEY)).getId();
        userRepository.deleteById(userId);
        session.removeAttribute(LOGIN_SESSION_KEY);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
