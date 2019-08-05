package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

@Slf4j
@Service
@Transactional
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserIdByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException());
        return user;
    }

    public void checkValidUser(UserDto userDto) throws IllegalArgumentException {
        log.debug(" >>> checkValidUser userDto : {}", userDto);
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(IllegalArgumentException::new);
        user.matchPassword(userDto);
    }
}
