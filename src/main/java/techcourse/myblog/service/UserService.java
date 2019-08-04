package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.DuplicateEmailException;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.MyPageDto;

import java.util.List;

import static java.util.Collections.unmodifiableList;

@Service
public class UserService {
    private static final String DUPLICATED_ERROR = "중복된 Email 입니다.";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User updateUserInfo(long id, MyPageDto userInfo) {
        User user = userRepository.findUserById(id);
        user.updateUserInfo(userInfo.getName());
        return user;
    }

    @Transactional
    public User save(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException(DUPLICATED_ERROR);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return unmodifiableList(userRepository.findAll());
    }

    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User findUserById(long id) {
        return userRepository.findUserById(id);
    }
}
