package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.application.exception.DuplicatedEmailException;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        verifyDuplicateEmail(user);
        return userRepository.save(user);
    }

    private void verifyDuplicateEmail(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(x -> { throw new DuplicatedEmailException(); });
    }

    public void update(User loginUser, User user) {
        userRepository.findByEmail(loginUser.getEmail())
                .ifPresent(existingUser -> {
                    loginUser.modifyName(user);
                    existingUser.modifyName(user);
                });
    }

    public void remove(User user) {
        userRepository.delete(user);
    }
}
