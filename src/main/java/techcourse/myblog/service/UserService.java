package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.web.controller.dto.LoginDto;
import techcourse.myblog.web.controller.dto.UserDto;
import techcourse.myblog.web.exception.CouldNotRegisterException;

import java.util.List;
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
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new CouldNotRegisterException("중복된 이메일입니다.");
        }
        return userRepository.save(new User(userDto.getName(), userDto.getEmail(), userDto.getPassword()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User update(String email, UserDto updatedUser) {
        User user = findByEmail(email).orElseThrow(NotFoundUserException::new);
        return user.update(updatedUser.getName());
    }

    public void remove(String email) {
        User user = findByEmail(email).orElseThrow(NotFoundUserException::new);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundUserException("이메일과 비밀번호를 다시 확인해주세요."));
        if (user.authenticate(loginDto.getEmail(), loginDto.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
