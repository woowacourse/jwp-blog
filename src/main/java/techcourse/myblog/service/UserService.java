package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
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

    public void createUser(UserDto userDto) {
        validateUniqueEmail(userDto);

        User user = UserAssembler.writeUser(userDto);
        userRepository.save(user);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (userRepository.countByEmail(userDto.getEmail()) > NOT_FOUND_RESULT) {
            throw new DuplicateEmailException();
        }
    }

    public List<UserDto> getAllUsers() {
        return UserAssembler.writeDtos(userRepository.findAll());
    }

    public User findUserByEmailAndPassword(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }

        return user;
    }

    public User findUserByEmailAndPassword(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        checkUserByEmail(email);
        checkPassword(email, password);

        return userRepository.findUserByEmailAndPassword(email, password);
    }

    private void checkUserByEmail(String email) {
        if (userRepository.countByEmail(email) == NOT_FOUND_RESULT) {
            throw new FailedLoginException("존재하지 않는 이메일입니다.");
        }
    }

    private void checkPassword(String email, String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
            throw new FailedLoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateUser(UserDto userDto) {
        String updatedName = userDto.getName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userRepository.findUserByEmail(email);
        User updatedUser = new User(user.getUserId(), updatedName, email, password);

        userRepository.save(updatedUser);
    }

    public void deleteUser(String email) {
        User user = userRepository.findUserByEmail(email);
        userRepository.deleteById(user.getUserId());
    }
}
