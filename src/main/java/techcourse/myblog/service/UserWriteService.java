package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.UserDto;

@Service
@Transactional
public class UserWriteService {
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";

    private final UserRepository userRepository;

    @Autowired
    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto user) {
        verifyDuplicateEmail(user);
        userRepository.save(user.toUser());
    }

    private void verifyDuplicateEmail(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedEmailException(DUPLICATED_USER_MESSAGE);
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
