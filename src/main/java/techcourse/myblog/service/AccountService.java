package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.UserDuplicateEmailException;
import techcourse.myblog.exception.UserNotFoundException;

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
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    }

    @Transactional
    public User save(UserDto userDto) {
        User user = userDto.toUser();
        if (isExistsByEmail(user.getEmail())) {
            throw new UserDuplicateEmailException();
        }
        return save(user);
    }

    @Transactional
    public User update(long id, UserDto userDto, User user) {
        User preUser = findById(id);
        return preUser.update(userDto, user);
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
