package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.exception.DuplicatedEmailException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserDto userDto) {
        verifyDuplicateEmail(userDto);
        return userRepository.save(userDto.toUser());
    }

    private void verifyDuplicateEmail(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(x -> { throw new DuplicatedEmailException(); });
    }

    public void update(User user, UserDto userDto) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    user.modifyName(userDto.toUser());
                    existingUser.modifyName(userDto.toUser());
                });
    }

    public void remove(User user) {
        userRepository.delete(user);
    }
}
