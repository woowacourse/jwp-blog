package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.SingUpDto;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String NOT_MATCH_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginDto loginByEmailAndPwd(UserRequestDto userRequestDto) {
        try {
            User user = userRepository.findByEmail(userRequestDto.getEmail())
                    .orElseThrow(() -> new UserException(NOT_EXIST_USER));
            if (user.isMatchPassword(userRequestDto)) {
                return new LoginDto(true, null, user.getName());
            }
            return new LoginDto(false, NOT_MATCH_PASSWORD);
        } catch (UserException e) {
            return new LoginDto(false, e.getMessage());
        }
    }

    public SingUpDto addUser(UserRequestDto userRequestDto) {
        try {
            return new SingUpDto(true, userRepository.save(userRequestDto.toEntity()).getId());
        } catch (UserException e) {
            return new SingUpDto(false, e.getMessage());
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
        return userRepository.findByEmail(session.getAttribute("userEmail").toString())
                .orElseThrow(UserException::new);
    }

    public void deleteUser(HttpSession session) {
        userRepository.delete(getUserBySession(session));
    }
}