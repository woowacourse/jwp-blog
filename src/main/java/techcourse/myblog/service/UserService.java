package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.WrongEmailAndPasswordException;
import techcourse.myblog.web.dto.LoginDto;
import techcourse.myblog.web.dto.UserDto;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
    }

    public User update(UserDto userDto) {
        User user = findByEmail(userDto.getEmail());
        return userRepository.save(user.update(userDto.create()));
    }

    public void remove(String email) {
        userRepository.delete(findByEmail(email));
    }

    public User login(LoginDto loginDto) throws WrongEmailAndPasswordException {
        if (userRepository.existsByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())) {
            return findByEmail(loginDto.getEmail());
        }
        throw new WrongEmailAndPasswordException("Email과 Password를 다시 확인해주세요.");
    }

    public boolean exists(String email) {
        return userRepository.existsByEmail(email);
    }
}
