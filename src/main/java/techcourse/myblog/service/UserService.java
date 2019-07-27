package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.DuplicatedUserException;
import techcourse.myblog.domain.exception.NotMatchPasswordException;
import techcourse.myblog.domain.exception.UnFoundUserException;
import techcourse.myblog.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

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
        return findBy(originalUser.getEmail())
                .modifyName(newName);
    }

    private User findBy(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UnFoundUserException("로그인 정보를 확인해주세요."));
    }

    public User login(User user) {
        User loginUser = findBy(user.getEmail());

        if (!loginUser.matchPassword(user)) {
            throw new NotMatchPasswordException("로그인 정보를 확인해주세요.");
        }

        return loginUser;
    }

    public void delete(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
