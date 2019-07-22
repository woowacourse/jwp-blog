package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.exception.SignUpException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.SessionUtil;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private static final String REGISTERED_EMAIL = "이미 등록된 이메일 입니다.";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        return userRepository.save(userRequestDto.toEntity());
    }

    private void checkRegisteredEmail(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new SignUpException(REGISTERED_EMAIL);
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