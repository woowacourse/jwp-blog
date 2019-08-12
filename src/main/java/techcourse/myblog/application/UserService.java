package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import techcourse.myblog.application.assembler.UserAssembler;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.DuplicatedEmailException;
import techcourse.myblog.application.exception.EditException;
import techcourse.myblog.application.exception.LoginException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.support.encrytor.EncryptHelper;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptHelper encryptHelper;
    private final UserAssembler userAssembler;

    public UserService(UserRepository userRepository, EncryptHelper encryptHelper,
                       UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.encryptHelper = encryptHelper;
        this.userAssembler = userAssembler;
    }

    public void save(UserRequest userRequest) {
        User user = createUser(userRequest);
        userRepository.save(user);
    }

    private User createUser(UserRequest userRequest) {
        userRequest.setPassword(encryptPassword(userRequest));
        checkDuplicatedEmail(userRequest);
        return userAssembler.convertToUser(userRequest);
    }

    private void checkDuplicatedEmail(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicatedEmailException("중복된 이메일입니다!");
        }
    }

    private String encryptPassword(UserRequest userRequest) {
        return encryptHelper.encrypt(userRequest.getPassword());
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(user -> userAssembler
                        .convertToUserResponse(user))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(userResponses);
    }

    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new LoginException("일치하는 email이 없습니다!"));

        checkPassword(loginRequest, user);
        return userAssembler.convertToUserResponse(user);
    }

    private void checkPassword(LoginRequest loginRequest, User user) {
        if (!encryptHelper.isMatch(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다!");
        }
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("존재하지 않는 회원입니다!"));
    }

    @Transactional
    public UserResponse modify(Long userId, String name) {
        User user = findById(userId);
        changeName(name, user);
        return userAssembler.convertToUserResponse(user);
    }

    private void changeName(String name, User user) {
        try {
            user.changeName(name);
        } catch (IllegalArgumentException e) {
            throw new EditException(e.getMessage());
        }
    }

    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }
}
