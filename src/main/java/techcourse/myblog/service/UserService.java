package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.web.controller.dto.LoginDto;
import techcourse.myblog.web.controller.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(UserDto userDto) {
        return userRepository.save(new User(userDto.getName(), userDto.getEmail(), userDto.getPassword()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User update(String email, UserDto updatedUser) {
        User user = findByEmail(email).orElseThrow(IllegalArgumentException::new);
        return user.update(updatedUser.getName());
    }

    public void remove(String email) {
        User user = findByEmail(email).orElseThrow(NoSuchElementException::new);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(IllegalArgumentException::new);
        if (user.authenticate(loginDto.getEmail(), loginDto.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
