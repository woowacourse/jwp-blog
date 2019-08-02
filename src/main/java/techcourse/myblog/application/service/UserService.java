package techcourse.myblog.application.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.application.exception.LoginFailException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User tryLogin(String email, String password) {
        Optional<User> user = getUserByEmail(email);
        if (user.isPresent() && user.get().authenticate(password)) {
            return user.get();
        }
        throw new LoginFailException("비밀번호 혹은 이메일이 잘못되었습니다.");
    }

    public Iterable<User> loadEveryUsers() {
        return userRepository.findAll();
    }


    public UserQueryResult tryRegister(String name, String email, String password) {
        return userRepository.findByEmail(email).map(ifSameEmailExists -> UserQueryResult.EMAIL_ALREADY_TAKEN)
                .orElseGet(() -> {
                    try {
                        userRepository.save(new User(name, email, password));
                        return UserQueryResult.SUCCESS;
                    } catch (IllegalArgumentException e) {
                        return UserQueryResult.INVALID_INPUT;
                    }
                });
    }

    @Transactional
    public User tryUpdate(String editedName, User user) {
        return userRepository.findByEmail(user.getEmail()).map(targetUser -> targetUser.update(editedName)).orElseThrow(IllegalArgumentException::new);
    }


    @Transactional
    public void delete(User user) {
        userRepository.deleteByEmail(user.getEmail());
    }
}