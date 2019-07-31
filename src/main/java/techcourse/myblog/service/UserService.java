package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import techcourse.myblog.domain.User;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.exception.UnFoundUserException;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {

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
                .orElseThrow(() -> new UnFoundUserException("로그인 정보를 확인해주세요."));
    }

    public User login(User user) {
        User loginUser = findByEmail(user.getEmail());
        checkPassword(user, loginUser);
        return loginUser;
    }

    private void checkPassword(User user, User loginUser) {
        if (!loginUser.matchPassword(user)) {
            throw new NotMatchPasswordException("로그인 정보를 확인해주세요.");
        }
    }

    public void delete(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
