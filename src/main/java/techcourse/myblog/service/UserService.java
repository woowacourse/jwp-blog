package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(UserDto userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        return userRepository.save(userDto.toUser());
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일주소 입니다.");
        }
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> findByEmailAndPassword(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
    }

    @Transactional
    public void update(User user, UserDto userDto) {
        User updateUser = userRepository.findById(user.getId()).get();
        user.update(userDto.toUser());
        updateUser.update(userDto.toUser());
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }
}
