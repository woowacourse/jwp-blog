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
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public UserService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedUserException("이미 존재하는 email입니다.");
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
                    return new UnFoundUserException("로그인 정보를 확인해주세요.");
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
            throw new NotMatchPasswordException("로그인 정보를 확인해주세요.");
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
