package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.exception.DuplicatedEmailException;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto user) {
        verifyDuplicateEmail(user);
        userRepository.save(user.toUser());
    }

    private void verifyDuplicateEmail(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedEmailException();
        }
    }

    public void update(User user, UserDto userDto) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    user.modifyName(userDto.getName());
                    existingUser.modifyName(userDto.getName());
                });
    }

    public void remove(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
