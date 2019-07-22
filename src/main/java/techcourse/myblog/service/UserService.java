package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.support.util.EncryptHelper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private EncryptHelper encryptHelper;

    public UserService(UserRepository userRepository, EncryptHelper encryptHelper) {
        this.userRepository = userRepository;
        this.encryptHelper = encryptHelper;
    }

    public User saveUser(UserRequest userRequest) {
        User user = createUser(userRequest);
        return userRepository.save(user);
    }

    private User createUser(UserRequest userRequest) {
        return new User(userRequest.getName(), userRequest.getEmail(),
                encryptHelper.encrypt(userRequest.getPassword()));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(UserLoginRequest userLoginRequest) {
        User user = userRepository.findUserByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new LoginException("일치하는 email이 없습니다!"));

        checkPassword(userLoginRequest, user);
        return user;
    }

    private void checkPassword(UserLoginRequest userLoginRequest, User user) {
        if (!encryptHelper.isMatch(userLoginRequest.getPassword(), user.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다!");
        }
    }

    @Transactional
    public User editUserName(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        changeName(name, user);
        return user;
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

    public List<FieldError> addSingUpError(UserRequest userRequest) {
       List<FieldError> errors = new ArrayList<>();

        if (isDuplicatedEmail(userRequest.getEmail())) {
            errors.add(new FieldError(
                    "userRequest",
                    "email",
                    "이메일이 중복입니다!"));
        }

        if (doesNotMatchPassword(userRequest.getPassword(), userRequest.getReconfirmPassword())) {
            errors.add(new FieldError(
                    "userRequest",
                    "reconfirmPassword",
                    "비밀번호가 일치하지 않습니다!"));
        }

       return errors;
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean doesNotMatchPassword(String password, String reconfirmPassword) {
        return !password.equals(reconfirmPassword);
    }
}
