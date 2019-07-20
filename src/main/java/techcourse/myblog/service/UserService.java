package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.web.controller.dto.LoginDto;
import techcourse.myblog.web.controller.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(UserDto userDto) {
        return userRepository.save(User.of(userDto));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User update(UserDto userDto) {
        User user = findByEmail(userDto.getEmail()).orElseThrow(IllegalArgumentException::new);
        return userRepository.save(user.update(userDto));
    }

    public void remove(String email) {
        User user = findByEmail(email).orElseThrow(IllegalArgumentException::new);
        userRepository.delete(user);
    }

    public Optional<User> checkUser(LoginDto loginDto) {
        return userRepository.findByEmail(loginDto.getEmail())
                .filter(user -> user.equalPassword(loginDto.getPassword()));
    }
}
