package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.SignupDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String NOT_MATCH_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginDto loginByEmailAndPwd(UserDto userDto) {
        try {
            User user = userRepository.findByEmail(userDto.getEmail())
                    .orElseThrow(() -> new UserException(NOT_EXIST_USER));
            if (user.isMatchPassword(userDto)) {
                return new LoginDto(true, null, user.getName());
            }
            return new LoginDto(false, NOT_MATCH_PASSWORD);
        } catch (UserException e) {
            return new LoginDto(false, e.getMessage());
        }
    }

    public SignupDto addUser(UserDto userDto) {
        try {
            return new SignupDto(true, userRepository.save(userDto.toEntity()).getId());
        } catch (UserException e) {
            return new SignupDto(false, e.getMessage());
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void updateUser(UserDto userDto, HttpSession session) {
        User user = getUserBySession(session);
        user.updateNameAndEmail(userDto.getName(), userDto.getEmail());
        userRepository.save(user);
    }

    private User getUserBySession(HttpSession session) {
        return userRepository.findByEmail(session.getAttribute("userEmail").toString()).orElseThrow(UserException::new);
    }

    public void deleteUser(HttpSession session) {
        userRepository.delete(getUserBySession(session));
    }
}
