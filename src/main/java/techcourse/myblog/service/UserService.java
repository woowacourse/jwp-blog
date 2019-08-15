package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.domain.utils.UserAssembler;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.support.encryptor.EncryptHelper;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;

    public UserService(UserRepository userRepository, UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    public User saveUser(UserRequest userRequest) {
        return userRepository.save(userAssembler.toUserFromUserRequest(userRequest));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        log.debug("begin");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginException("email 없음"));

        return user;
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
}
