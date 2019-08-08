package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.EditException;
import techcourse.myblog.application.exception.LoginException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.support.encrytor.EncryptHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EncryptHelper encryptHelper;

    public UserService(UserRepository userRepository, EncryptHelper encryptHelper) {
        this.userRepository = userRepository;
        this.encryptHelper = encryptHelper;
    }

    public void saveUser(UserRequest userRequest) {
        User user = userRequest.toEntity(this::encryptPassword);
        userRepository.save(user);
    }

    private String encryptPassword(String password) {
        return encryptHelper.encrypt(password);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NoUserException("사용자를 찾을 수 없습니다: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());

        return userResponses;
    }

    @Transactional(readOnly = true)
    public UserResponse checkLogin(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new LoginException("일치하는 email이 없습니다!"));

        checkPassword(loginRequest, user);
        return UserResponse.from(user);
    }

    private void checkPassword(LoginRequest loginRequest, User user) {
        if (!encryptHelper.isMatch(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다!");
        }
    }

    public UserResponse editUserName(Long userId, String name) {
        User user = findById(userId);
        changeName(name, user);
        return UserResponse.from(user);
    }

    private void changeName(String name, User user) {
        try {
            user.changeName(name);
        } catch (IllegalArgumentException e) {
            throw new EditException(e.getMessage());
        }
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
