package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.exception.UnFoundUserException;
import techcourse.myblog.repository.UserRepository;

@Slf4j
@Service
@Transactional
public class UserService {

    private static final String CHECK_LOGIN_INFO_ERROR = "로그인 정보를 확인해주세요.";
    private static final String DUPLICATE_EMAIL_ERROR = "이미 존재하는 email입니다.";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedUserException(DUPLICATE_EMAIL_ERROR);
        }
    }

    public User update(User originalUser, String newName) {
        return findByEmail(originalUser.getEmail())
                .modifyName(newName);
    }

    private User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> {
                    log.debug(email);
                    return new UnFoundUserException(CHECK_LOGIN_INFO_ERROR);
                });
    }

    public User login(User user) {
        User loginUser = findByEmail(user.getEmail());
        checkPassword(user, loginUser);
        return loginUser;
    }

    private boolean checkPassword(User user, User loginUser) {
        if (!loginUser.matchPassword(user)) {
            log.debug(user.toString());
            log.debug(loginUser.toString());
            throw new NotMatchPasswordException(CHECK_LOGIN_INFO_ERROR);
        }
        return true;
    }

    public boolean delete(User user) {
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
