package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.SignUpDto;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.SessionUtil;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignUpDto addUser(UserRequestDto userRequestDto) {
        try {
            return new SignUpDto(true, userRepository.save(userRequestDto.toEntity()).getId());
        } catch (UserException e) {
            return new SignUpDto(false, e.getMessage());
        }
    }

    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public void updateUser(UserRequestDto userRequestDto, HttpSession session) {
        User user = getUserBySession(session);
        user.updateNameAndEmail(userRequestDto.getName(), userRequestDto.getEmail());
        userRepository.save(user);
    }

    private User getUserBySession(HttpSession session) {
        return userRepository.findByEmail(SessionUtil.getAttribute(session, "userEmail").toString())
                .orElseThrow(UserException::new);
    }

    public void deleteUser(HttpSession session) {
        userRepository.delete(getUserBySession(session));
    }
}