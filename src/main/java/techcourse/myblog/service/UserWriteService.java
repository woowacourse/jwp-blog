package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.exception.SignUpFailedException;

@Service
@Transactional
public class UserWriteService {
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";

    private final UserRepository userRepository;
    private final UserReadService userReadService;

    public UserWriteService(UserRepository userRepository, UserReadService userReadService) {
        this.userRepository = userRepository;
        this.userReadService = userReadService;
    }

    public void save(User user) {
        if (userReadService.isExist(user)) {
            throw new SignUpFailedException(DUPLICATED_USER_MESSAGE);
        }
        userRepository.save(user);
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
