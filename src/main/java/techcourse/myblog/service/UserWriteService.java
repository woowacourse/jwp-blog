package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;
    private final UserReadService userReadService;

    public UserWriteService(UserRepository userRepository, UserReadService userReadService) {
        this.userRepository = userRepository;
        this.userReadService = userReadService;
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
        User originUser = userReadService.findByEmail(loginUser.getEmail());
        originUser.modifyName(userDto.getName());
        loginUser.modifyName(userDto.getName());
    }

    public void remove(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
