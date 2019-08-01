package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    @Autowired
    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        verifyDuplicateEmail(user);
        userRepository.save(user);
    }

    private void verifyDuplicateEmail(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(x -> {
            throw new DuplicatedEmailException();
        });
    }

    public void update(User loginUser, UserDto userDto) {
        userRepository.findByEmail(loginUser.getEmail())
                .ifPresent(existingUser -> {
                    loginUser.modifyName(userDto.getName());
                    existingUser.modifyName(userDto.getName());
                });
    }

    public void remove(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
