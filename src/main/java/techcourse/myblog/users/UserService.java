package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long save(UserDto userDto) {
        User user = new User();

        BeanUtils.copyProperties(userDto, user);

        return userRepository.save(user).getId();
    }
}


