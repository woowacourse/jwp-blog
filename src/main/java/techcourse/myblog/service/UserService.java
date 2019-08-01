package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.FailedPasswordVerificationException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private static final int NOT_FOUND_RESULT = 0;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        validateUniqueEmail(userDto);
        validatePasswordConfirm(userDto);
        User user = UserAssembler.writeUser(userDto);
        userRepository.save(user);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (userRepository.countByEmail(userDto.getEmail()) > NOT_FOUND_RESULT) {
            throw new DuplicateEmailException();
        }
    }

    private void validatePasswordConfirm(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            throw new FailedPasswordVerificationException();
        }
    }

    public List<UserDto> getAllUsers() {
        return UserAssembler.writeDtos(userRepository.findAll());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    public User findUserByEmailAndPassword(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        checkEmail(email);
        return userRepository.findUserByEmailAndPassword(email, password)
                .orElseThrow(() -> new FailedLoginException("비밀번호가 일치하지 않습니다."));
    }

    private void checkEmail(String email) {
        if (userRepository.countByEmail(email) == NOT_FOUND_RESULT) {
            throw new FailedLoginException("존재하지 않는 이메일입니다.");
        }
    }

    @Transactional
    public User update(UserDto userDto) {
        User updatedUser = new User(userDto.getUserId(), userDto.getName(), userDto.getEmail(), userDto.getPassword());
        User user = userRepository.findUserByEmail(updatedUser.getEmail())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        return user.update(updatedUser);
    }

    public void deleteByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        userRepository.deleteById(user.getUserId());
    }
}
