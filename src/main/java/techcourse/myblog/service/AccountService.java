package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.List;

@Slf4j
@Service
public class AccountService {
    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);

    }

    @Transactional
    public User save(UserDto userDto) {
        User user = userDto.toUser();
        if (isExistsByEmail(user.getEmail())) {
            throw new IllegalArgumentException();
        }
        return save(user);
    }

    @Transactional
    public User update(UserDto userDto, User user) {
        user.checkMatch(userDto);
        user.update(userDto);
        return user;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    private boolean isExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private User save(User user) {
        return userRepository.save(user);
    }
}
